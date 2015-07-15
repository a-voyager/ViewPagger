package com.example.viewpager;

import android.content.Context;
import android.os.SystemClock;

public class ViewPagerUtils {

	private int startX;
	private int startY;
	private int disX;
	private int disY;
	private long startTime;
	private boolean isFinished;
	private int duration = 500;
	private long currX;
	private long currY;

	public ViewPagerUtils(Context context) {
	}

	public void start(int startX, int startY, int disX, int disY) {
		this.startX = startX;
		this.startY = startY;
		this.disX = disX;
		this.disY = disY;
		this.startTime = SystemClock.uptimeMillis();
		this.isFinished = false;
	}

	public boolean computeScrollOffset() {
		if (isFinished) {
			return false;
		}
		long passTime = SystemClock.uptimeMillis() - startTime;
		if (passTime < duration) {
			// 当前的位置 = 开始的位置 + 移动的距离（距离 = 速度*时间）
			currX = startX + passTime * disX / duration;
			currY = startY + passTime * disY / duration;
		} else {
			currX = startX + disX;
			currY = startY + disY;
			isFinished = true;
		}
		return true;
	}

	public long getCurrX() {
		return currX;
	}

	public void setCurrX(long currX) {
		this.currX = currX;
	}

	public long getCurrY() {
		return currY;
	}

	public void setCurrY(long currY) {
		this.currY = currY;
	}

}
