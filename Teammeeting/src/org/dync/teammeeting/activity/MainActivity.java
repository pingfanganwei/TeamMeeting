package org.dync.teammeeting.activity;

import java.util.ArrayList;
import java.util.List;

import org.dync.teammeeting.R;
import org.dync.teammeeting.adapter.SwipeListAdapter;
import org.dync.teammeeting.adapter.SwipeListAdapter.SwipeListOnClick;
import org.dync.teammeeting.app.NetWork;
import org.dync.teammeeting.app.TeamMeetingApp;
import org.dync.teammeeting.helper.dlg.CopyLinkDialog;
import org.dync.teammeeting.structs.EventType;
import org.dync.teammeeting.structs.ExtraType;
import org.dync.teammeeting.structs.RoomItem;
import org.dync.teammeeting.utils.RandData;
import org.dync.teammeeting.utils.ScreenUtils;
import org.dync.teammeeting.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ypy.eventbus.EventBus;

public class MainActivity extends Activity {
	public final static int UPDATE_COPY_LINK = 0X01;
	public final static int UPDATE_RENAME_SHOW = 0X02;
	public final static int UPDATE_LISTVIEW_SCROLL = 0X03;
	public final static int UPDATE_RENAME_END = 0X04;
	private final static String TAG = "MainActivity";
	private boolean mDebug = TeamMeetingApp.mIsDebug;
	private RelativeLayout mRlMain;
	private ListView mListView;
	private TextView mRoomCancel;
	private Button mGetRoom;
	private EditText mCreateRoom;
	private SwipeListAdapter mAdapter;
	private CopyLinkDialog mCopyLinkDialog;

	private Context mContext;
	private List<RoomItem> mRoomData = new ArrayList<RoomItem>();
	private InputMethodManager mIMM;
	private NetWork mNetWork;
	private long mExitTime = 0;
	private Boolean mCreateRoomFlag = false;
	private Boolean mReNameFlag = false;
	private String mSign;
	private boolean mSoftInputFlag = false;
	private int mVisibleItem;
	private int mItemHeight;
	private int mDy;
	private int mPosition;

	private Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_COPY_LINK:
				mCopyLinkDialog.dismiss();
				break;
			case UPDATE_RENAME_SHOW:
				int position = msg.getData().getInt("position");
				mRoomData.get(position).setmMeetType2(2);
				mAdapter.notifyDataSetChanged();
				if (mDebug)
					Log.e(TAG, "UPDATE_RENAME_SHOW");
				break;
			case UPDATE_LISTVIEW_SCROLL:
				mAdapter.notifyDataSetChanged();
				mListView.animate().translationY(-mDy).setDuration(2000);
				break;
			case UPDATE_RENAME_END:
				mAdapter.notifyDataSetChanged();
				if(mDy==0)
				 mListView.smoothScrollToPositionFromTop(0, 0, 1000);
				else{
				 mListView.animate().translationYBy(mDy).setDuration(10);	
				 mListView.smoothScrollToPositionFromTop(0, 0, 500);
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		inint();
	}

	/**
	 * inint
	 */
	private void inint() {
		EventBus.getDefault().register(this);
		mContext = this;
		mNetWork = new NetWork(this);
		mSign = TeamMeetingApp.getMyself().getmAuthorization();

		mIMM = (InputMethodManager) MainActivity.this
				.getSystemService(MainActivity.INPUT_METHOD_SERVICE);

		mRlMain = (RelativeLayout) findViewById(R.id.rl_main);
		mCreateRoom = (EditText) findViewById(R.id.et_create_room);
		mRoomCancel = (TextView) findViewById(R.id.tv_cancel_create_room);
		mListView = (ListView) findViewById(R.id.lv_listView);

		mGetRoom = (Button) findViewById(R.id.btn_get_room);
		mGetRoom.setOnClickListener(mOnClickListener);
		mRoomCancel.setOnClickListener(mOnClickListener);

		mRoomData = TeamMeetingApp.getMyself().getmRoomList();
		mAdapter = new SwipeListAdapter(mContext, mRoomData, mSwipeListOnClick);
		mListView.setAdapter(mAdapter);
		
		

		mRlMain.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						
						if(isKeyboardShown(mRlMain.getRootView())){
							if (mDebug)
								Log.e(TAG, "isKeyboardShown open keyboard");
							mSoftInputFlag = true;
						}
						else{
							if (mDebug)
								Log.e(TAG, "isKeyboardShown close keyboard");
							if(mReNameFlag&&mSoftInputFlag){
								mUIHandler.sendEmptyMessageDelayed(UPDATE_RENAME_END, 500);
								mReNameFlag =false;
								mRoomData.get(mPosition).setmMeetType2(1);
							}
							
							if (mSoftInputFlag) {
								mCreateRoom.setVisibility(View.GONE);
								mRoomCancel.setVisibility(View.GONE);
								mSoftInputFlag = false;
							}
							
						
						}
			
					}
				});
	}
	
	/**
	 * isKeyboardShown
	 * 
	 * @param rootView
	 * @return   
	 * 			true   soft keyboard is open
	 * 			false  soft keyboard is open
	 */
	
	private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        Log.e(TAG, "rootView.getBottom() "+rootView.getBottom()+" r.bottom "+r.bottom);
        return heightDiff > softKeyboardHeight * dm.density;
	}	
	
	/**
	 * listViewSetScroll
	 * 
	 * @param position
	 */

	private void listViewSetScroll(int position) {

		mItemHeight = getItemHeight(mListView);
		float temp = mListView.getHeight()/(float)getItemHeight(mListView);
		mVisibleItem = (int) Math.ceil(temp);
		
		if(mDebug)
			Log.e(TAG, " temp "+temp+" mVisibleItem "+mVisibleItem);
		
		int maxItemTop = mAdapter.getCount() - mVisibleItem;

		  if(mDebug) 
			  Log.e("xbl", " maxitemTop " +
				  maxItemTop+" position "+position+" mListView.getHeight() "+mListView.getHeight()+
				  " mVisibleItem "+mVisibleItem+" mAdapter.getCount() "+mAdapter.getCount());
		 

		if (position <= maxItemTop) {
			mDy = 0;
			mListView.smoothScrollToPositionFromTop(position, 0, 2000);
		} else {
			
			int incompleteItemheight = mListView.getHeight()-(mVisibleItem-1)*mItemHeight;
			mDy = mItemHeight * (position - maxItemTop-1)+incompleteItemheight;
			Log.e(TAG, "maxItemTop "+maxItemTop+" incompleteItemheight "+incompleteItemheight);
			//mListView.smoothScrollToPositionFromTop(maxItemTop, 0, 1000);
			//mListView.smoothScrollToPosition(maxItemTop-1);
			mListView.setSelection(mListView.getBottom());
			//mListView.animate().translationY(-mDy).setDuration(2000);
			mUIHandler.sendEmptyMessageDelayed(UPDATE_LISTVIEW_SCROLL, 1500);
			
		}

	}

	/**
	 * getItemHeight
	 * 
	 * @param listView
	 * @return
	 */
	private int getItemHeight(final ListView listView) {
		View view = mAdapter.getView(0, null, listView);

		view.measure(0, 0);
		int i =(int)ScreenUtils.dip2Dimension(10.0f, this);
		Log.e(TAG, " i "+i);
		return view.getMeasuredHeight();
	}

	/**
	 * hideKeyboard
	 * 
	 * @return
	 */
	private boolean hideKeyboard() {
		if (mIMM.isActive(mCreateRoom)) {

			mIMM.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			mIMM.restartInput(mCreateRoom);

			// mIMM.hideSoftInputFromWindow(mCreateRoom.getWindowToken(), 0);
			mCreateRoom.setVisibility(View.GONE);
			mRoomCancel.setVisibility(View.GONE);
			mGetRoom.setVisibility(View.VISIBLE);

			return true;
		}

		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.tv_cancel_create_room:
				mIMM.hideSoftInputFromWindow(mCreateRoom.getWindowToken(), 0);

				mCreateRoom.setVisibility(View.GONE);
				mRoomCancel.setVisibility(View.GONE);
				mGetRoom.setVisibility(View.VISIBLE);
				break;

			case R.id.btn_get_room:

				mRoomCancel.setVisibility(View.VISIBLE);
				mCreateRoom.setVisibility(view.VISIBLE);
				mCreateRoom.setText("");

				mCreateRoom.setFocusable(true);
				mCreateRoom.setFocusableInTouchMode(true);
				mCreateRoom.requestFocus();
				mIMM.showSoftInput(mCreateRoom, 0);
				mCreateRoom.setOnEditorActionListener(editorActionListener);

				break;

			default:
				break;
			}
		}
	};

	private SwipeListOnClick mSwipeListOnClick = new SwipeListOnClick() {

		@Override
		public void onItemClickListener(View v, int position) {
			// TODO Auto-generated method stub

			String sign;
			String meetingId;
			if (hideKeyboard()) {
				return;
			}

			Intent intent;
			switch (v.getId()) {
			case R.id.fl_front:
				// closeAllLayout();
				intent = new Intent(mContext, MeetingActivity.class);
				mContext.startActivity(intent);
				break;

			case R.id.btn_delete:
				Utils.showToast(mContext, "	: " + position + " del");
				sign = TeamMeetingApp.getMyself().getmAuthorization();
				meetingId = mRoomData.get(position).getmMeetingId();
				mNetWork.deleteRoom(sign, meetingId);
				mRoomData.remove(position);
				mAdapter.notifyDataSetChanged();
				break;

			case R.id.imgbtn_more_setting:

				intent = new Intent(mContext, RoomSettingActivity.class);
				String meetingName = mRoomData.get(position).getmMeetName();
				meetingId = mRoomData.get(position).getmMeetingId();
				intent.putExtra("meetingName", meetingName);
				intent.putExtra("meetingId", meetingId);
				intent.putExtra("position", position);
				startActivityForResult(intent,
						ExtraType.REQUEST_CODE_ROOM_SETTING);
				((Activity) mContext).overridePendingTransition(
						R.anim.activity_open_enter, R.anim.activity_open_exit);

				break;
			case R.id.et_rename:

				EditText reName = (EditText) v.findViewById(R.id.et_rename);
				String newName = reName.getText().toString();
				String oldName = mRoomData.get(position).getmMeetName();

				if (mDebug) {
					Log.e(TAG, "et_rename newName " + newName);
				}

				if (!newName.equals(oldName)) {

					sign = TeamMeetingApp.getMyself().getmAuthorization();
					meetingId = mRoomData.get(position).getmMeetingId();
					mNetWork.updateMeetRoomName(sign, meetingId, newName);

					mRoomData.get(position).setmMeetName(newName);

				}

				mRoomData.get(position).setmMeetType2(1);
				mIMM.hideSoftInputFromWindow(reName.getWindowToken(), 0);
				mUIHandler.sendEmptyMessageDelayed(UPDATE_RENAME_END, 500);
				mReNameFlag = false ; 
				break;

			default:
				break;
			}

		}
	};

	/**
	 * soft keyboard Listener
	 */
	OnEditorActionListener editorActionListener = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

			String roomName = mCreateRoom.getText().toString();
			if (roomName.length() == 0 || roomName == null) {
				roomName = "Untitled room";
			}

			String sign = TeamMeetingApp.getMyself().getmAuthorization();
			mNetWork.applyRoom(sign, roomName, "0", "","123");

			mAdapter.notifyDataSetChanged();

			mIMM.hideSoftInputFromWindow(mCreateRoom.getWindowToken(), 0);
			mCreateRoom.setVisibility(View.GONE);
			mRoomCancel.setVisibility(View.GONE);
			mCreateRoomFlag = true;

			return false;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		Log.e(TAG, "onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (hideKeyboard()) {
				Log.e(TAG, "打开");
				return false;
			} else {
				Log.e(TAG, "关闭");
			}

			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, R.string.exit_once_more, 0).show();
				mExitTime = System.currentTimeMillis();
			} else {
				String sign = TeamMeetingApp.getMyself().getmAuthorization();
				// signout
				mNetWork.signOut(sign);

			}
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * onActivityResult
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		String sign;
		int position;
		String meetingId;

		if (mDebug)
			Log.e(TAG, "resultCode " + resultCode);
		switch (resultCode) {

		case ExtraType.RESULT_CODE_ROOM_SETTING_MESSAGE_INVITE:

			break;
		case ExtraType.RESULT_CODE_ROOM_SETTING_WEIXIN_INVITE:

			break;
		case ExtraType.RESULT_CODE_ROOM_SETTING_COPY_LINK:
			mCopyLinkDialog = new CopyLinkDialog(this);
			mCopyLinkDialog.show();
			mUIHandler.sendEmptyMessageDelayed(UPDATE_COPY_LINK, 3000);
			break;
		case ExtraType.RESULT_CODE_ROOM_SETTING_NOTIFICATION:

			break;
		case ExtraType.RESULT_CODE_ROOM_SETTING_RENAME:
			mReNameFlag = true ; 
			sign = TeamMeetingApp.getMyself().getmAuthorization();
			position = data.getIntExtra("position", 0);
			mPosition = position;
			meetingId = data.getStringExtra("meetingId");
			String meetingName = data.getStringExtra("meetingName");
			listViewSetScroll(position);
			Message msg = new Message();
			msg.what = UPDATE_RENAME_SHOW;
			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			msg.setData(bundle);
			mUIHandler.sendMessageDelayed(msg, 3000);
			// mUIHandler.sendEmptyMessageDelayed(UPDATE_RENAME_SHOW, 5500);

			break;
		case ExtraType.RESULT_CODE_ROOM_SETTING_DELETE:
			sign = TeamMeetingApp.getMyself().getmAuthorization();
			position = data.getIntExtra("position", 0);
			meetingId = data.getStringExtra("meetingId");

			mNetWork.deleteRoom(sign, meetingId);
			mRoomData.remove(position);
			mAdapter.notifyDataSetChanged();

			break;

		default:
			break;
		}

	};

	/**
	 * For EventBus callback.
	 */
	public void onEventMainThread(Message msg) {
		switch (EventType.values()[msg.what]) {

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
			mRoomData.clear();
			mRoomData.addAll(TeamMeetingApp.getMyself().getmRoomList());
			mAdapter.notifyDataSetChanged();
			

			if (mCreateRoomFlag) {
				Intent intent = new Intent(MainActivity.this,
						InvitePeopleActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_open_enter,
						R.anim.activity_open_exit);
				mCreateRoomFlag = false;
			}

			break;

		case MSG_GET_ROOM_LIST_FAILED:
			if (mDebug)
				Log.e(TAG, "MSG_GET_ROOM_LIST_FAILED");
			break;

		case MSG_APPLY_ROOM_SUCCESS:
			if (mDebug)
				Log.e(TAG, "MSG_APPLY_ROOM_SUCCESS");
			mNetWork.getRoomList(mSign, 1 + "", 20 + "");

			break;

		case MSG_APPLY_ROOMT_FAILED:
			if (mDebug)
				Log.e(TAG, "MSG_APPLY_ROOMT_FAILED");
			break;

		case MSG_UPDATE_MEET_ROOM_NAME_SUCCESS:
			if (mDebug)
				Log.e(TAG, "MSG_UPDATE_MEET_ROOM_NAME_SUCCESS");

			break;

		case MSG_UPDATE_MEET_ROOM_NAME_FAILED:
			if (mDebug)
				Log.e(TAG, "MSG_UPDATE_MEET_ROOM_NAME_FAILED");
			break;

		default:
			break;
		}
	}

}
