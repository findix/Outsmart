package com.find1x.outsmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		if (arg1.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//			// ---------����һ��Intent,��һ��Activity;
//			Intent intent_Activity = new Intent(arg0, My_android_Activity.class);
//			// ����������Action,����ǿ�Ƶģ�
//			intent_Activity.setAction("android.intent.action.MAIN");
//			// ���category��,����ǿ�Ƶģ�
//			intent_Activity.addCategory("android.intent.category.LAUNCHER");
//			/*
//			 * �������ڲ���Ļ�����չ���������־��ǿ���Ե����ã�����ӣ�
//			 * Ϊ��Ҫ������Activity���������������˲�����������ʱΪActivity�����µ�ջ��
//			 */
//			intent_Activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//			// ����activity
//			arg0.startActivity(intent_Activity);

			// --------����һ��Intent��������һ��Service;

			Intent intent_service = new Intent(arg0, SmsReceiverService.class);
			// �����ڷ����������һЩ�û�����Ҫ֪���Ĳ�����������¡�
			arg0.startService(intent_service);

		}
	}

}
