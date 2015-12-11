package org.dync.teammeeting.activity;

import org.dync.teammeeting.R;
import org.dync.teammeeting.ui.BottomMenu;
import org.dync.teammeeting.ui.BottomMenu.OnTouchSpeedListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class InvitePeopleActivity extends Activity
{

	private final static String TAG = "MainActivity";
	private TextView mCloseTV, mMessageInviteTV, mWeixinInviteTV, mCopyLinkTV;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_people);
		inint();

	}

	private void inint()
	{

		mCloseTV = (TextView) findViewById(R.id.tv_close);
		mMessageInviteTV = (TextView) findViewById(R.id.tv_invite_message);
		mWeixinInviteTV = (TextView) findViewById(R.id.tv_invite_weixin);
		mCopyLinkTV = (TextView) findViewById(R.id.tv_copy_link);

		mCloseTV.setOnClickListener(mOnClickListener);
		mMessageInviteTV.setOnClickListener(mOnClickListener);
		mWeixinInviteTV.setOnClickListener(mOnClickListener);
		mCopyLinkTV.setOnClickListener(mOnClickListener);

		BottomMenu bottomMenu = (BottomMenu) findViewById(R.id.bottomMenu);
		bottomMenu.setOnTouchQuickSpeedListener(onTouchSpeedListener);

	}

	/**
	 * Touch slide Listener
	 */
	OnTouchSpeedListener onTouchSpeedListener = new OnTouchSpeedListener()
	{

		@Override
		public void touchSpeed(int velocityX, int velocityY)
		{
			finishActivity();
		}
	};
	private OnClickListener mOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View view)
		{
			Intent intent = null;
			switch (view.getId())
			{
			case R.id.tv_close:
				finishActivity();
				return;

			case R.id.tv_invite_message:
				intent = new Intent(InvitePeopleActivity.this, ShareActivity.class);

				break;
			case R.id.tv_invite_weixin:
				intent = new Intent(InvitePeopleActivity.this, ShareActivity.class);
				break;
			case R.id.tv_copy_link:

				intent = new Intent(InvitePeopleActivity.this, ShareActivity.class);
				break;

			default:
				break;
			}

			startActivity(intent);
			finishActivity();

		}
	};

	public void finishActivity()
	{
		finish();
		overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);

	};
}
