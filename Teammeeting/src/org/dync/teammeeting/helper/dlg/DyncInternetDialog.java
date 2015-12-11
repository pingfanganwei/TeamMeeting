package org.dync.teammeeting.helper.dlg;

import org.dync.teammeeting.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

public class DyncInternetDialog extends Dialog {

    private Button mBtnConnectInternet;
    private View mContentView;

    public DyncInternetDialog(Context context ,final InternetOnClickListener listener) {
        super(context, R.style.RtkProgressiveDialog);
        mContentView = getLayoutInflater().inflate(R.layout.no_internet_view, null);
        setContentView(mContentView);
        mContentView.setBackgroundResource(R.drawable.internet_dialog_bg);
        mBtnConnectInternet = (Button) findViewById(R.id.btn_connect_internet);
        setCanceledOnTouchOutside(false);
        mBtnConnectInternet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.internetOnClick(v);
			}
		});
 
    }
    


    public interface InternetOnClickListener{
    	public void internetOnClick(View v);
    }


    public void setBackground(int resid) {
        if (resid > 0) {
            mContentView.setBackgroundResource(resid);
        }
    }


}
