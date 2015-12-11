package org.dync.teammeeting.activity;


import org.dync.teammeeting.R;
import org.dync.teammeeting.app.NetWork;
import org.dync.teammeeting.app.TeamMeetingApp;
import org.dync.teammeeting.helper.dlg.DyncInternetDialog;
import org.dync.teammeeting.helper.dlg.DyncProgressiveDialog;
import org.dync.teammeeting.helper.dlg.DyncInternetDialog.InternetOnClickListener;
import org.dync.teammeeting.structs.EventType;
import org.dync.teammeeting.structs.NetType;
import org.dync.teammeeting.utils.LocalUserInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.ypy.eventbus.EventBus;

public class StartFlashActivity extends Activity {

	private static final String TAG = "StartFlashActivity";
	private boolean mDebug =TeamMeetingApp.mIsDebug;
	private NetWork mNetWork;
	private DyncProgressiveDialog mProgressiveDialog;
	private DyncInternetDialog mInternetDialog;
	private View mView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mView = View.inflate(this, R.layout.activity_start_flash, null);
		setContentView(mView);
		
		inint();

		
	}
	
	/**
	 * inint
	 */
	private void inint(){
		
		EventBus.getDefault().register(this);
		mNetWork = new NetWork(this);
		
		AlphaAnimation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
		alphaAnim.setDuration(1000);
		mView.setAnimation(alphaAnim);
		alphaAnim.setAnimationListener(mAnimationListener);
		
		
	}
	
	private InternetOnClickListener mInternetListener = new InternetOnClickListener() {
		
		@Override
		public void internetOnClick(View v) {
			// TODO Auto-generated method stub
			if(mDebug){
				Log.e(TAG, "internetOnClick");
				mInternetDialog.dismiss();;
			}
		}
	};
	
	
	
	private AnimationListener mAnimationListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationRepeat(Animation arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationEnd(Animation arg0) {
			// TODO Auto-generated method stub
			//startProgress();
			String userid = TeamMeetingApp.getTeamMeetingApp().getDevId();
			mNetWork.inint(userid, "2", "2", "2", "TeamMeeting");
		}
	};
	
	
	/**
	 * open Progress
	 */
	private void startProgress() {
		
		mProgressiveDialog = new DyncProgressiveDialog(this);
		//mProgressiveDialog.setMessage(R.string.str_p2p_connecting);
		mProgressiveDialog.setCancelable(false);
		mProgressiveDialog.show();
	}

	/**
	 * close Progress
	 */
	private void stopProgress() {
		if (mProgressiveDialog != null) {
			mProgressiveDialog.dismiss();
			mProgressiveDialog = null;
		}
	}

	
	/**
	 * interfacejump
	 */
	private void interfacejump(){
		
		boolean firstLogin = LocalUserInfo.getInstance(StartFlashActivity.this).getUserInfoBoolean("firstLogin");
		Intent intent;
		if(firstLogin==false)
		{	
			intent = new Intent(StartFlashActivity.this, GuideActivity.class);
			LocalUserInfo.getInstance(StartFlashActivity.this).setUserInfoBoolean("firstLogin",true);
		}
		else 
		{
			 intent = new Intent(StartFlashActivity.this, MainActivity.class);	
		}
		
		startActivity(intent);
		finish();
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	/**
	 * For EventBus callback.
	 */
	public void onEventMainThread(Message msg) {
		switch (EventType.values()[msg.what]) {
		case MSG_ININT_SUCCESS:
			if (mDebug)
				Log.e(TAG, "MSG_ININT_SUCCESS");
			String sign = TeamMeetingApp.getMyself().getmAuthorization();
			mNetWork.getRoomList(sign, 1 + "", 20 + "");
			break;

		case MSG_ININT_FAILED:
			if (mDebug)
				Log.e(TAG, "MSG_ININT_FAILED");

			break;
		case MSG_SIGNOUT_SUCCESS:
			if (mDebug)
				Log.e(TAG, "MSG_SIGNOUT_SUCCESS");
			finish();
			System.exit(0);
			break;

		case MSG_SIGNOUT_FAILED:
			if (mDebug)
				Log.e(TAG, "MSG_SIGNOUT_FAILED");
			break;

		case MSG_GET_ROOM_LIST_SUCCESS:
			if (mDebug)
				Log.e(TAG, "MSG_GET_ROOM_LIST_SUCCESS");
			stopProgress();
			interfacejump();
			break;

		case MSG_GET_ROOM_LIST_FAILED:
			if (mDebug)
				Log.e(TAG, "MSG_GET_ROOM_LIST_FAILED");
			break;
			
		case MSG_NET_WORK_TYPE:
			
			if(mDebug)
				Log.e(TAG, "MSG_NET_WORK_TYPE");
			
			int type = msg.getData().getInt("net_type");
			if (type == NetType.TYPE_NULL.ordinal()) {
				stopProgress();
				mInternetDialog = new DyncInternetDialog(this,mInternetListener);
				mInternetDialog.show();
				
			} else {
				
			}
			
			break;

		default:
			break;
		}
	}
	
	

}
