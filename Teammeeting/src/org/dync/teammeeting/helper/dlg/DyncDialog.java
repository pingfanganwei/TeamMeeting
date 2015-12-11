package org.dync.teammeeting.helper.dlg;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;

/**
 * the common dialog class
 * 
 * @author SkylineRunner
 *
 */
public class DyncDialog {
	/**
	 * create the dialog with String and listener
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param strPositive
	 * @param strNegative
	 * @param positiveListener
	 * @param negativeListener
	 */
	public static void showDialog(Context context, String title, String message, String strPositive, String strNegative,
			OnClickListener positiveListener, OnClickListener negativeListener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton(strPositive, positiveListener);
		builder.setNegativeButton(strNegative, negativeListener);
		builder.create().show();
	}

	/**
	 * create the dialog with resource id and listener
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param strPositive
	 * @param strNegative
	 * @param positiveListener
	 * @param negativeListener
	 */
	public static void showDialog(Context context, int title, int message, int strPositive, int strNegative,
			OnClickListener positiveListener, OnClickListener negativeListener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton(strPositive, positiveListener);
		builder.setNegativeButton(strNegative, negativeListener);
		builder.create().show();
	}

	/**
	 * create the dialog with resource icon、 resource string and listener
	 * 
	 * @param context
	 * @param icon
	 * @param title
	 * @param message
	 * @param strPositive
	 * @param strNegative
	 * @param positiveListener
	 * @param negativeListener
	 */
	public static void showDialog(Context context, int icon, int title, int message, int strPositive, int strNegative,
			OnClickListener positiveListener, OnClickListener negativeListener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setIcon(icon);
		builder.setPositiveButton(strPositive, positiveListener);
		builder.setNegativeButton(strNegative, negativeListener);
		builder.create().show();
	}

	/**
	 * create the dialog with Drawable、 string and listener
	 * 
	 * @param context
	 * @param icon
	 * @param title
	 * @param message
	 * @param strPositive
	 * @param strNegative
	 * @param positiveListener
	 * @param negativeListener
	 */
	public static void showDialog(Context context, Drawable icon, int title, int message, int strPositive,
			int strNegative, OnClickListener positiveListener, OnClickListener negativeListener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setIcon(icon);
		builder.setPositiveButton(strPositive, positiveListener);
		builder.setNegativeButton(strNegative, negativeListener);
		builder.create().show();
	}
}
