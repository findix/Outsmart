package com.find1x.outsmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		if (arg1.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//			// ---------ÉùÃ÷Ò»¸öIntent,´ò¿ªÒ»¸öActivity;
//			Intent intent_Activity = new Intent(arg0, My_android_Activity.class);
//			// ÉèÖÃÆô¶¯µÄAction,²»ÊÇÇ¿ÖÆµÄ£»
//			intent_Activity.setAction("android.intent.action.MAIN");
//			// Ìí¼Ócategory£¬,²»ÊÇÇ¿ÖÆµÄ£»
//			intent_Activity.addCategory("android.intent.category.LAUNCHER");
//			/*
//			 * Èç¹û»î¶¯ÊÇÔÚ²»»î¶¯µÄ»·¾³ÏÂÕ¹¿ª£¬Õâ¸ö±êÖ¾ÊÇÇ¿ÖÆÐÔµÄÉèÖÃ£¬±ØÐë¼Ó£»
//			 * Îª¸ÕÒªÆô¶¯µÄActivityÉèÖÃÆô¶¯²ÎÊý£¬´Ë²ÎÊýÉêÃ÷Æô¶¯Ê±ÎªActivity¿ª±ÙÐÂµÄÕ»¡£
//			 */
//			intent_Activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//			// Æô¶¯activity
//			arg0.startActivity(intent_Activity);

			// --------ÉùÃ÷Ò»¸öIntentÓÃÒÔÆô¶¯Ò»¸öService;

			Intent intent_service = new Intent(arg0, SmsReceiverService.class);
			// ¿ÉÒÔÔÚ·þÎñÀïÃæ½øÐÐÒ»Ð©ÓÃ»§²»ÐèÒªÖªµÀµÄ²Ù×÷£¬±ÈÈç¸üÐÂ¡£
			arg0.startService(intent_service);

		}
	}

}
