/**
 * SwipeListAdapter.java [V 1.0.0]
 * classes:com.example.menu.swipe.SwipeListAdapter
 * Zlang Create at 2015-12-1.上午9:57:09 
 */
package org.dync.teammeeting.adapter;

import java.util.HashSet;
import java.util.List;

import org.dync.teammeeting.R;
import org.dync.teammeeting.activity.InvitePeopleActivity;
import org.dync.teammeeting.activity.MainActivity;
import org.dync.teammeeting.activity.MeetingActivity;
import org.dync.teammeeting.activity.RoomSettingActivity;
import org.dync.teammeeting.structs.RoomItem;
import org.dync.teammeeting.ui.swipe.FrontLayout;
import org.dync.teammeeting.ui.swipe.SwipeLayout;
import org.dync.teammeeting.ui.swipe.SwipeLayout.SwipeListener;
import org.dync.teammeeting.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * 
 * @author ZLang <br/>
 *         create at 2015-12-1 上午9:57:09 <br/>
 *         
 */
public class SwipeListAdapter extends CommonAdapter<RoomItem> {

	private SwipeListOnClick mSwipeListOnClick;
	HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();
	private Context mContext ;
	private InputMethodManager mIMM;

	public SwipeListAdapter(Context context, List<RoomItem> data,
			SwipeListOnClick mswipeListOnClick) {
		super(context, data);
		mContext = context;
		mSwipeListOnClick = mswipeListOnClick;
	}

	public interface SwipeListOnClick {
		public void onItemClickListener(View v, int position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		if (convertView != null) {
			mHolder = (ViewHolder) convertView.getTag();
		} else {
			convertView = (SwipeLayout) mInflater.inflate(
					R.layout.item_room_adapter, null);
			mHolder = ViewHolder.fromValues(convertView);
			convertView.setTag(mHolder);
		}

		SwipeLayout swipeLayout = (SwipeLayout) convertView;
		swipeLayout.close(false, false);
		swipeLayout.setSwipeListener(mSwipeListener);

		mHolder.mRoomName.setText("" + mDatas.get(position).getmMeetName());
		mHolder.mRoomTime.setText("Joined: "
				+ mDatas.get(position).getmJoinTime());
		mHolder.mRoomPeopleCount.setText(""
				+ mDatas.get(position).getmMemNumber());
		
		if(mDatas.get(position).getmMeetType2()==2){
			mHolder.mReName.setVisibility(View.VISIBLE);
			mHolder.mRLShowView.setVisibility(View.GONE);
			mHolder.mReName.setText(mDatas.get(position).getmMeetName());
			mHolder.mReName.setFocusable(true);
			mHolder.mReName.setFocusableInTouchMode(true);
			mHolder.mReName.requestFocus();
			mIMM = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
			mIMM.showSoftInput(mHolder.mReName, 0);
		}
		else{
			mHolder.mReName.setVisibility(View.GONE);
			mHolder.mRLShowView.setVisibility(View.VISIBLE);
		}
		

		mHolder.mItemLayout.setTag(position);
		mHolder.mItemLayout.setOnClickListener(onActionClick);
		mHolder.mRoomDel.setTag(position);
		mHolder.mRoomDel.setOnClickListener(onActionClick);
		mHolder.mRoomDel.setTag(position);
		mHolder.mMoreSetting.setOnClickListener(onActionClick);
		mHolder.mMoreSetting.setTag(position);
		mHolder.mReName.setTag(position);
		mHolder.mReName.setOnEditorActionListener(onEditorListener);
		return swipeLayout;
	}
	
	/**
	 * 
	 */
	OnEditorActionListener onEditorListener = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			Integer postion = (Integer) v.getTag();
			mSwipeListOnClick.onItemClickListener(v, postion);
			return false;
		}
	};

	/**
	 * Click callback
	 */
	OnClickListener onActionClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Integer postion = (Integer) v.getTag();
			mSwipeListOnClick.onItemClickListener(v, postion);

		}
	};

	/**
	 * slide listener
	 */
	SwipeListener mSwipeListener = new SwipeListener() {
		@Override
		public void onOpen(SwipeLayout swipeLayout) {
			// Utils.showToast(context, "onOpen");
			mUnClosedLayouts.add(swipeLayout);
		}

		@Override
		public void onClose(SwipeLayout swipeLayout) {
			// Utils.showToast(context, "onClose");
			mUnClosedLayouts.remove(swipeLayout);
		}

		@Override
		public void onStartClose(SwipeLayout swipeLayout) {
			// Utils.showToast(context, "onStartClose");
		}

		@Override
		public void onStartOpen(SwipeLayout swipeLayout) {
			// Utils.showToast(mContext, "onStartOpen");
			closeAllLayout();
			mUnClosedLayouts.add(swipeLayout);
		}

	};

	public void closeAllLayout() {
		if (mUnClosedLayouts.size() == 0)
			return;

		for (SwipeLayout l : mUnClosedLayouts) {
			l.close(true, false);
		}
		mUnClosedLayouts.clear();
	}

	/**
	 * org.dync.teammeeting.adapter.ViewHolder <br/>
	 * View Holder
	 * 
	 * @author ZLang <br/>
	 *         create at 2015-12-1 下午12:42:48
	 */
	private static class ViewHolder {

		EditText mReName;
		RelativeLayout mRLShowView;

		TextView mRoomName;
		TextView mRoomTime;
		TextView mRoomPeopleCount;

		ImageButton mMoreSetting;
		ImageButton mRoomDel;
		FrontLayout mItemLayout;

		private ViewHolder(EditText reName, RelativeLayout rlShowView,
				TextView roomName, TextView roomTime, TextView roomPeopleCount,
				ImageButton moreSetting, ImageButton roomDel,
				FrontLayout itemLayout) {
			super();
			this.mReName = reName;
			this.mRLShowView = rlShowView;
			this.mRoomName = roomName;
			this.mRoomTime = roomTime;
			this.mRoomPeopleCount = roomPeopleCount;
			this.mMoreSetting = moreSetting;
			this.mRoomDel = roomDel;
			this.mItemLayout = itemLayout;
		}

		public static ViewHolder fromValues(View view) {
			return new ViewHolder((EditText) view.findViewById(R.id.et_rename),
					(RelativeLayout) view.findViewById(R.id.rl_show_view),
					(TextView) view.findViewById(R.id.tv_room_name),
					(TextView) view.findViewById(R.id.tv_room_time),
					(TextView) view.findViewById(R.id.tv_people_count),
					(ImageButton) view.findViewById(R.id.imgbtn_more_setting),
					(ImageButton) view.findViewById(R.id.btn_delete),
					(FrontLayout) view.findViewById(R.id.fl_front));
		}
	}

}
