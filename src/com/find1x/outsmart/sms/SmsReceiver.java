package com.find1x.outsmart.sms;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import com.find1x.outsmart.DialogActivity;
import com.find1x.outsmart.analysis.GetUserTime;
import com.find1x.outsmart.segmentation.Persistence;

/**
 * class name��SmsReceiver<BR>
 * class description�����ݿ�ı������<BR>
 * PS�������ݸı��ʱ��ִ�������change����<BR>
 * 
 * @version 1.00
 */
public class SmsReceiver extends ContentObserver {
	/**
	 * Activity����
	 */
	private Context context;

	public SmsReceiver(Handler handler, Context context) {
		super(handler);
		this.context = context;
	}

	@Override
	public void onChange(boolean selfChange) {

		super.onChange(selfChange);
		final String SMS_URI_INBOX = "content://sms/inbox";
		Uri uri = Uri.parse(SMS_URI_INBOX);
		String[] projectionSMS = new String[] { "_id", "address", "person",
				"body", "date", "type" };
		final Cursor cur = context.getContentResolver().query(uri,
				projectionSMS, null, null, "date desc");
		cur.moveToFirst();
		String content = cur.getString(cur.getColumnIndex("body"));
		String address = cur.getString(cur.getColumnIndex("address"));
		String person = cur.getString(cur.getColumnIndex("person"));
		long date = cur.getLong(cur.getColumnIndex("date"));
		int id = cur.getInt(cur.getColumnIndex("_id"));
		cur.close();
		// Log.i("���������ݿ�仯", id + " " + address + " " + content + " " + person);
		// ��������һ��Activityһ��Ҫ��Intent����FLAG_ACTIVITY_NEW_TASK�ı�־����Ȼ�ᱨ��
		Intent intent = new Intent(context, DialogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, DialogActivity.class);
		intent.putExtra("content", content);
		intent.putExtra("address", address);
		intent.putExtra("person", person);
		intent.putExtra("date", date);
		Persistence smsId = new Persistence("sms.db");
		// Log.i("this id", id + "");
		// Log.i("last id", smsId.getValue() + "");
		// Log.i("setting",new Persistence("Setting.db").getValue()+"");
		// Log.i("isMeeting",new GetUserTime(content).isMeeting()+"");
		if (id > smsId.getValue()) {
			smsId.changeValue(id);
			if (new Persistence("Setting.db").getValue() == 1
					&& new GetUserTime(content).isMeeting()) {
				context.startActivity(intent);
			}
		}

	}
}
