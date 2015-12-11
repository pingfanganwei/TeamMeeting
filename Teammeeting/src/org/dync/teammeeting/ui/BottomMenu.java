package org.dync.teammeeting.ui;

import org.dync.teammeeting.app.TeamMeetingApp;
import org.dync.teammeeting.utils.ScreenUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 
 * org.dync.teammeeting.ui.BottomMenu
 * 
 * @author ZLang <br/>
 *         后期努力完善 有部分bug 不影响使用 create at 2015-11-30 下午7:01:22
 */
public class BottomMenu extends ViewGroup
{

	private Context mContext;
	private static String TAG = "xbl";
	private boolean mDebug = TeamMeetingApp.mIsDebug;
	private Scroller mScroller = null;

	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	private int mTouchState = TOUCH_STATE_REST;
	// --------------------

	private int mTouchSlop = 400;
	private float mLastionMotionX = 0;
	private float mLastMotionY = 0;

	private VelocityTracker mVelocityTracker = null; // Touch Tracker
	private int mMenuHeight;
	private int mScreenHeight;

	private int mMaxTop;
	private int mTop;
	private int mBottom;
	private int speedClose = 5000;
	private int mDyration = 800;
	private int mXDiff;

	public BottomMenu(Context context)
	{
		super(context);
		mContext = context;
		init();
	}

	public BottomMenu(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		init();
	}

	@Override
	public void computeScroll()
	{
		if (mScroller.computeScrollOffset())
		{
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	private void init()
	{
		mScroller = new Scroller(mContext);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

		final float density = mContext.getResources().getDisplayMetrics().density;

		int tagHeight = Integer.parseInt(this.getTag().toString());
		mMenuHeight = (int) (tagHeight * density);
		if (mDebug)
		{
			Log.e(TAG, "mMenuHeight--" + mMenuHeight);
		}
		mScreenHeight = ScreenUtils.getScreenHeight(getContext());

		if (tagHeight < 400)
		{
			mTop = mScreenHeight - mMenuHeight;
			mMaxTop = 0;
		} else
		{
			mTop = mScreenHeight - mMenuHeight / 2;
			mMaxTop = mMenuHeight / 2;
		}
		if (mDebug)
			Log.e(TAG, "---menuHeight --" + mMenuHeight);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		setMeasuredDimension(width, height);

		for (int i = 0; i < getChildCount(); i++)
		{
			View child = getChildAt(i);
			child.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	// layout过程
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{

		for (int i = 0; i < getChildCount(); i++)
		{
			View child = getChildAt(i);

			child.layout(l, mTop, r, mTop + mMenuHeight);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		// final int action = ev.getAction();
		return super.dispatchTouchEvent(ev);
	}

	// @Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		final int action = ev.getAction();

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action)
		{
		case MotionEvent.ACTION_MOVE:
			// Log.e(TAG, "onInterceptTouchEvent move");
			final int xDiff = (int) Math.abs(mLastionMotionX - x);

			if (xDiff > 0)
			{
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;

		case MotionEvent.ACTION_DOWN:
			// Log.e(TAG, "onInterceptTouchEvent down");
			mLastionMotionX = x;
			mLastMotionY = y;
			// Log.e(TAG, mScroller.isFinished() + "");
			if (mScroller != null)
			{
				if (!mScroller.isFinished())
				{
					mScroller.abortAnimation();
				}
			}
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
			// mTouchState = TOUCH_STATE_SCROLLING;

			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			Log.e(TAG, "onInterceptTouchEvent up or cancel");
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		if (mDebug)
		{
			Log.e(TAG, mTouchState + "====" + TOUCH_STATE_REST);
		}

		return mTouchState != TOUCH_STATE_REST;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mVelocityTracker == null)
		{
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		float x = event.getX();
		float y = event.getY();
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:

			if (mScroller != null)
			{
				if (!mScroller.isFinished())
				{
					mScroller.abortAnimation();
				}
			}
			mLastMotionY = y;
			mLastionMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			// Log.e(TAG, "onTouchEvent = MOVE" + getScrollY());
			int detaX = (int) (mLastionMotionX - x);
			int detaY = (int) (mLastMotionY - y);

			if (Math.abs(detaY) < 500)
			{
				scrollBy(0, detaY);
			}
			mLastionMotionX = x;
			mLastMotionY = y;

			// mTouchState = TOUCH_STATE_REST;

			break;
		case MotionEvent.ACTION_UP:
			Log.e(TAG, "onTouchEvent = UP" + getScrollY());
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();
			int velocityY = (int) velocityTracker.getYVelocity();

			// Log.d(TAG, "--velocityY--" + velocityY);
			if (velocityY > speedClose)
			{

				if (touchSpeedListener != null)
				{

					scrollBy(0, -mScreenHeight);

					touchSpeedListener.touchSpeed(velocityX, velocityY);
				}
			} else
			{
				ScrollView(velocityX, velocityY);
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.e(TAG, "onTouchEvent = CANCEL" + getScrollY());
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	private void ScrollView(int velocityX, int velocityY)
	{

		if (getScrollY() < mMaxTop && getScrollY() >= 0)
		{
			mScroller.fling(getScrollX(), getScrollY(), 0, (int) (-velocityY), 0, 0, 0, mMaxTop);

		} else
		{
			if (getScrollY() < 0)
			{
				mScroller.startScroll(0, getScrollY(), 0, -(getScrollY()), Math.abs(mDyration));
			} else
			{

				mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() - mMaxTop),
						Math.abs(mDyration));
			}

		}

		invalidate();
		if (mVelocityTracker != null)
		{
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}

	}

	public interface OnTouchSpeedListener
	{
		public void touchSpeed(int velocityX, int velocityY);
	};

	private OnTouchSpeedListener touchSpeedListener;

	public void setOnTouchQuickSpeedListener(OnTouchSpeedListener touchSpeedListener)
	{
		this.touchSpeedListener = touchSpeedListener;
	}
}
