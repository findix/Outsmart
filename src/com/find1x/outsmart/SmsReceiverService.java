package com.find1x.outsmart;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import com.find1x.outsmart.sms.SmsReceiver;

public class SmsReceiverService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// System.out.println("�����Ѵ���");
		SmsReceiver smsReceiver = new SmsReceiver(new Handler(), this);
		this.getContentResolver().registerContentObserver(
				Uri.parse("content://sms"), true, smsReceiver);
	}

}
