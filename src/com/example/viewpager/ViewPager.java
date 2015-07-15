package com.example.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ClickableViewAccessibility")
public class ViewPager extends ViewGroup {

	private Context ctx;
	private GestureDetector gestureDetector;

	/**
	 * 当前viewpager的id号
	 */
	private int currId = 0;

	private int preX = 0;

	private ViewPagerUtils utils;
	protected boolean isFling;
	private viewPagerChanged pagerChanged;

	/*
	 * onInterceptHoverEvent()里的x、y的位置，和x、y方向上的位移
	 */
	private int x = 0, dx = 0;
	private int y = 0, dy = 0;

	public ViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.ctx = context;
		initView();
	}

	private void initView() {
		System.out.println("init view");
		utils = new ViewPagerUtils(ctx);
		gestureDetector = new GestureDetector(ctx, new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				System.out.println("onSingleTapUp");
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				System.out.println("onShowPress");

			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				System.out.println("onScroll");
				System.out.println("dis=" + distanceX);
				scrollBy((int) distanceX, 0);
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				System.out.println("onLongPress");

			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				isFling = true;
				if (velocityX > 0 && currId > 0) {
					--currId;
				} else if (velocityX < 0 && currId < getChildCount() - 1) {
					++currId;
				}
				moveToDest(currId);
				return false;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			view.layout(0 + i * getWidth(), 0, getWidth() + i * getWidth(),
					getHeight());
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		super.onTouchEvent(event);
		gestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			preX = (int) event.getX();
			dx = dy = 0;
			x = (int) event.getX();
			y = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:
			if (!isFling) {
				int currX = (int) event.getX();
				int nextId = 0;
				if (currX - preX > getWidth() / 3) {
					nextId = --currId;
				} else if (preX - currX > getWidth() / 3) {
					nextId = ++currId;
				} else {
					nextId = currId;
				}
				moveToDest(nextId);
			}
			isFling = false;
			break;

		default:
			break;
		}
		return true;
	}

	public void moveToDest(int nextId) {
		currId = nextId >= 0 ? nextId : 0;
		currId = nextId <= getChildCount() - 1 ? nextId : getChildCount();
		// scrollTo(currId * getWidth(), 0);
		if (pagerChanged != null) {
			pagerChanged.moveToDest(currId);
		}
		int dis = currId * getWidth() - getScrollX();
		utils.start(getScrollX(), 0, dis, 0);
		invalidate();
	}

	/*
	 * 原理 invalidate()执行时会调用该方法，期间有一定时间差（视手机性能）
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();
		if (utils.computeScrollOffset()) {
			scrollTo((int) utils.getCurrX(), 0);
			invalidate();
		}
	}

	public viewPagerChanged getPagerChanged() {
		return pagerChanged;
	}

	public void setPagerChanged(viewPagerChanged pagerChanged) {
		this.pagerChanged = pagerChanged;
	}

	public interface viewPagerChanged {
		void moveToDest(int id);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 注意
			// 此处与onTouchEvent()冲突，在onTouchEvent()中初始化
			gestureDetector.onTouchEvent(ev);// 解决跳屏问题
			break;
		case MotionEvent.ACTION_MOVE:
			final int curX = (int) ev.getX();
			final int curY = (int) ev.getY();
			dx += (int) Math.abs(curX - x);
			dy += (int) Math.abs(curY - y);
			x = curX;
			y = curY;
			if (dx >= dy && dx > 50) {
				return true;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

}
