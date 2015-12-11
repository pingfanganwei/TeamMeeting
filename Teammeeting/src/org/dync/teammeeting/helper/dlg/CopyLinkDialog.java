package org.dync.teammeeting.helper.dlg;

import org.dync.teammeeting.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CopyLinkDialog extends Dialog {

    private TextView mTextView;
    private ImageView mImageView;
    private View mContentView;

    public CopyLinkDialog(Context context) {
        super(context, R.style.RtkProgressiveDialog);
        mContentView = getLayoutInflater().inflate(R.layout.copy_link_view, null);
        setContentView(mContentView);
        mContentView.setBackgroundResource(R.drawable.custom_progressive_dialog_bg);
        mTextView = (TextView) findViewById(R.id.tv_copy_link);
        mImageView = (ImageView) findViewById(R.id.iv_copy_link);
        setCanceledOnTouchOutside(false);
        setMessage(0);
    }

    public CopyLinkDialog setMessage(int id) {
        if (0 == id) {
        	mTextView.setText(R.string.str_copy_link);
        } else if (0 < id) {
        	mTextView.setText(id);
        }
        return this;
    }

    public void setBackground(int resid) {
        if (resid > 0) {
            mContentView.setBackgroundResource(resid);
        }
    }


}
