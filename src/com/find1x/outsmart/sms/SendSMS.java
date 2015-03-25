package com.find1x.outsmart.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.text.TextUtils;

import java.util.ArrayList;

public class SendSMS {
	/** ��������յĹ㲥 **/
	String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
	Context mContext = null;

	public SendSMS() {
	}

	public SendSMS(final Context mContext, String number, String text) {
		this.mContext = mContext;

		if (!TextUtils.isEmpty(number) && !TextUtils.isEmpty(text)) {
			sendMessage(number, text);
		}
	}

	private void sendMessage(String phoneNumber, String message) {
		// ---sends an SMS message to another device---
		SmsManager sms = SmsManager.getDefault();

		// create the sentIntent parameter
		Intent sentIntent = new Intent(SENT_SMS_ACTION);
		PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
				sentIntent, 0);

		// create the deilverIntent parameter
		Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		PendingIntent deliverPI = PendingIntent.getBroadcast(mContext, 0,
				deliverIntent, 0);

		// ����������ݳ���70���ַ� ���������Ų�ɶ������ŷ��ͳ�ȥ
		if (message.length() > 70) {
			ArrayList<String> msgs = sms.divideMessage(message);
			for (String msg : msgs) {
				sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
			}
		} else {
			sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
		}
	}

}
