package com.example.viewpager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.viewpager.ViewPager.viewPagerChanged;

public class MainActivity extends Activity {

	private int image[] = new int[] { R.drawable.p1, R.drawable.p2,
			R.drawable.p3, R.drawable.p4, R.drawable.p5 };
	private ViewPager myVp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		miui();
		initView();
	}

	private void miui() {
		Window window = getWindow();
		Class clazz = window.getClass();
		try {
			int tranceFlag = 0;
			int darkModeFlag = 0;
			Class layoutParams = Class
					.forName("android.view.MiuiWindowManager$LayoutParams");

			Field field = layoutParams
					.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
			tranceFlag = field.getInt(layoutParams);

			field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			darkModeFlag = field.getInt(layoutParams);

			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
					int.class);
			// 只需要状态栏透明
			// extraFlagField.invoke(window, tranceFlag, tranceFlag); //或
			// 状态栏透明且黑色字体
			extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag
					| darkModeFlag);
			// 清除黑色字体
			extraFlagField.invoke(window, 0, darkModeFlag);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		myVp = (ViewPager) findViewById(R.id.vp_main);
		final RadioGroup rg_main = (RadioGroup) findViewById(R.id.rg_main);
		for (int i = 0; i < image.length; i++) {
			ImageView imageView = new ImageView(MainActivity.this);
			imageView.setBackgroundResource(image[i]);
			myVp.addView(imageView);
		}

		myVp.addView(getLayoutInflater().inflate(R.layout.vp_view, null), 3);

		for (int i = 0; i < myVp.getChildCount(); i++) {
			RadioButton rbtn = new RadioButton(MainActivity.this);
			rg_main.addView(rbtn);
			rbtn.setId(i);
			if (i == 0) {
				rbtn.setChecked(true);
			}
		}
		myVp.setPagerChanged(new viewPagerChanged() {

			@Override
			public void moveToDest(int id) {
				((RadioButton) rg_main.getChildAt(id)).setChecked(true);
			}
		});

		rg_main.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				myVp.moveToDest(checkedId);
			}
		});

	}

}
