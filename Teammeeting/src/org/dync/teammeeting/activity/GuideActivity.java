package org.dync.teammeeting.activity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.dync.teammeeting.R;
import org.dync.teammeeting.R.drawable;
import org.dync.teammeeting.R.id;
import org.dync.teammeeting.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 
 * @author zhangqilu
 *
 */

public class GuideActivity extends Activity
{
	private ViewPager viewPager;
	private List<ImageView> imageViews;

	private String[] titles;
	private int[] imageResId;
	private List<View> dots;
	// private TextView tv_title;
	private Button mGuideButton;
	private int currentItem = 0;

	// An ExecutorService that can schedule commands to run after a given delay,
	// or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_view_page);

		imageResId = new int[] { R.drawable.view_page_1, R.drawable.view_page_2,
				R.drawable.view_page_3 };

		imageViews = new ArrayList<ImageView>();

		for (int i = 0; i < imageResId.length; i++)
		{
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_INSIDE);
			imageViews.add(imageView);
		}

		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));

		mGuideButton = (Button) findViewById(R.id.btn_guide);
		mGuideButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{

				Intent intent = new Intent(GuideActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		viewPager = (ViewPager) findViewById(R.id.vp);
		viewPager.setAdapter(new MyAdapter());

		viewPager.setOnPageChangeListener(new MyPageChangeListener());

	}

	private class MyPageChangeListener implements OnPageChangeListener
	{
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position)
		{
			if (position == 2)
				mGuideButton.setVisibility(View.VISIBLE);
			else
				mGuideButton.setVisibility(View.GONE);
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0)
		{

		}

		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}
	}

	private class MyAdapter extends PagerAdapter
	{

		@Override
		public int getCount() {
			return imageResId.length;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
}