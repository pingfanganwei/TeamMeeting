package org.dync.teammeeting.activity;

import org.dync.teammeeting.R;
import org.dync.teammeeting.R.anim;
import org.dync.teammeeting.R.id;
import org.dync.teammeeting.R.layout;
import org.dync.teammeeting.structs.ExtraType;
import org.dync.teammeeting.ui.BottomMenu;
import org.dync.teammeeting.ui.BottomMenu.OnTouchSpeedListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RoomSettingActivity extends Activity implements android.view.View.OnClickListener
{
	private TextView mTvRoomName;
	private TextView mTvJoninRoom;
	private TextView mTvIniviteMessage;
	private TextView mTvInviteWeixin;
	private TextView mTvCopyLink;
	private TextView mTvNotifications;
	private TextView mTvRenameRoom;
	private TextView mTvDeleteRoom;
	private TextView mTvClose;
	private String mMeetingName;
	private String mMeetingId;
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_setting);

		initLayout();
	}

	void initLayout()
	{

		mMeetingName = getIntent().getStringExtra("meetingName");
		mMeetingId = getIntent().getStringExtra("meetingId");
		mPosition = getIntent().getIntExtra("position", 0);

		mTvRoomName = (TextView) findViewById(R.id.tv_room_name);
		mTvRoomName.setText(mMeetingName);
		mTvJoninRoom = (TextView) findViewById(R.id.tv_join_room);
		mTvJoninRoom.setOnClickListener(this);

		mTvIniviteMessage = (TextView) findViewById(R.id.tv_invite_message);
		mTvIniviteMessage.setOnClickListener(this);

		mTvInviteWeixin = (TextView) findViewById(R.id.tv_invite_weixin);
		mTvInviteWeixin.setOnClickListener(this);

		mTvCopyLink = (TextView) findViewById(R.id.tv_copy_link);
		mTvCopyLink.setOnClickListener(this);

		mTvNotifications = (TextView) findViewById(R.id.tv_notifications);
		mTvNotifications.setOnClickListener(this);

		mTvRenameRoom = (TextView) findViewById(R.id.tv_rename_room);
		mTvRenameRoom.setOnClickListener(this);

		mTvDeleteRoom = (TextView) findViewById(R.id.tv_delete_room);
		mTvDeleteRoom.setOnClickListener(this);

		mTvClose = (TextView) findViewById(R.id.tv_close);
		mTvClose.setOnClickListener(this);

		BottomMenu bottomMenu = (BottomMenu) findViewById(R.id.bottomMenu);
		bottomMenu.setOnTouchQuickSpeedListener(onTouchSpeedListener);
	}

	/**
	 * 　Touch slide Listener
	 */
	OnTouchSpeedListener onTouchSpeedListener = new OnTouchSpeedListener()
	{

		@Override
		public void touchSpeed(int velocityX, int velocityY)
		{
			finish();
			overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
		}
	};

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		switch (view.getId())
		{
		case R.id.tv_close:
			finishActivity();
			return;

		case R.id.tv_join_room:

			intent = new Intent(RoomSettingActivity.this, MeetingActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_invite_message:
			// SMS
			intent = new Intent(RoomSettingActivity.this, ShareActivity.class);

			break;
		case R.id.tv_invite_weixin:
			// weixin
			intent = new Intent(RoomSettingActivity.this, ShareActivity.class);
			break;
		case R.id.tv_copy_link:

			intent = new Intent();
			setResult(ExtraType.RESULT_CODE_ROOM_SETTING_COPY_LINK, intent);
			break;
		case R.id.tv_notifications:
			// The meeting notifications
			intent = new Intent(RoomSettingActivity.this, ShareActivity.class);

			break;
		case R.id.tv_rename_room:

			intent = new Intent();
			intent.putExtra("position", mPosition);
			intent.putExtra("meetingId", mMeetingId);
			intent.putExtra("meetingName", mMeetingName);
			setResult(ExtraType.RESULT_CODE_ROOM_SETTING_RENAME, intent);
			break;
		case R.id.tv_delete_room:

			intent = new Intent();
			intent.putExtra("position", mPosition);
			intent.putExtra("meetingId", mMeetingId);
			setResult(ExtraType.RESULT_CODE_ROOM_SETTING_DELETE, intent);
			break;

		default:
			break;
		}

		finishActivity();
	}

	private void finishActivity()
	{
		finish();
		overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);

	};
}
