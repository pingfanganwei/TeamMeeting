package org.dync.teammeeting.ui;

import org.dync.teammeeting.R;
import org.dync.teammeeting.utils.ScreenUtils;
import org.webrtc.DataChannel.Init;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 
 * @author ZLang create at <br/>
 *         zhangqilu updata 2015-12-10 下午8:15:02
 */
public class PopupWindowCustom
{

	private Context mContext;
	private PopupWindow mWindow;
	private LinearLayout mContentView;
	private ImageButton mClosePopup, mInviteMessagePopup, mInviteWeixinPopup;
	private TextView mCopyLinkText;
	private Button mCopyLinkButton;
	private OnPopupWindowClickListener mOnListener;
	private int margin_top = 110; // The default distance from the bottom
	private int mScreenWidth;
	private int mScreenHeight;

	public PopupWindowCustom(Context context, View btnView, View topbar,
			OnPopupWindowClickListener listener)
	{
		// TODO Auto-generated constructor stub
		mContext = context;
		mOnListener = listener;
		initData();
		inintView();
		layoutPopup(btnView, topbar);
	}

	/**
	 * 
	 */
	private void initData()
	{
		mScreenWidth = ScreenUtils.getScreenWidth(mContext);
		mScreenHeight = ScreenUtils.getScreenHeight(mContext);
	}

	public interface OnPopupWindowClickListener
	{

		public void onPopupClickListener(View view);
	}

	private void layoutPopup(View btnView, View topbar)
	{

		int popupWidth = mScreenWidth - btnView.getWidth();

		int[] location = new int[2];
		btnView.getLocationOnScreen(location);
		int btn_weight = btnView.getWidth() / 2;
		int locationY = location[1] + btn_weight;
		int popupheight = mScreenHeight - (locationY * 2);

		mWindow = new PopupWindow(mContext);

		final float density = mContext.getResources().getDisplayMetrics().density;

		int marginTop = (int) (margin_top * density);
		mWindow.setHeight(popupheight - marginTop);
		mWindow.setWidth(popupWidth);
		mWindow.setContentView(mContentView);
		mWindow.setFocusable(true);
		mWindow.setTouchable(true);
		mWindow.setOutsideTouchable(true);
		mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mWindow.setAnimationStyle(R.style.popwin_anim_style);
		mWindow.showAsDropDown(topbar, btn_weight, btn_weight / 2);
	}

	private void inintView()
	{

		if (mScreenHeight < mScreenWidth)
		{
			mContentView = (LinearLayout) View.inflate(mContext, R.layout.popup_layout_horiz, null);
			margin_top = 0;
		} else
		{
			mContentView = (LinearLayout) View.inflate(mContext, R.layout.popup_layout, null);
			margin_top = 110;
		}

		mClosePopup = (ImageButton) mContentView.findViewById(R.id.ibtn_close);
		mInviteMessagePopup = (ImageButton) mContentView.findViewById(R.id.ibtn_message);
		mInviteWeixinPopup = (ImageButton) mContentView.findViewById(R.id.ibtn_weixin);
		mCopyLinkText = (TextView) mContentView.findViewById(R.id.tv_copy);
		mCopyLinkButton = (Button) mContentView.findViewById(R.id.btn_copy);

		mClosePopup.setOnClickListener(mOnClickListener);
		mInviteMessagePopup.setOnClickListener(mOnClickListener);
		mInviteWeixinPopup.setOnClickListener(mOnClickListener);
		mCopyLinkButton.setOnClickListener(mOnClickListener);
		mCopyLinkText.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View view)
		{
			// TODO Auto-generated method stub
			mOnListener.onPopupClickListener(view);
		}
	};

	/**
	 * Dismiss the popup window.
	 */
	public void dismiss()
	{
		mWindow.dismiss();
	}

}
