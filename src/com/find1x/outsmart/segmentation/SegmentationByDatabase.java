package com.find1x.outsmart.segmentation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.find1x.outsmart.db.CopyAndGetSQL;

import java.util.ArrayList;
import java.util.Calendar;

public class SegmentationByDatabase {
	CopyAndGetSQL database = new CopyAndGetSQL();
	SQLiteDatabase db;

	public SegmentationByDatabase(Context context) {
		db = database.openDatabase(context);
		// long ftime = Calendar.getInstance().getTimeInMillis();// ��ʼʱ��
		Log.i("���ڸ�ʲô", "��ʼ");
		// Log.i("����ʱ��", Calendar.getInstance().getTimeInMillis() - ftime + "");
	}

	public String[] getWords(String str, Context context) {

		ArrayList<String> list = getWordsbyArrayList(str, context);
		return (String[]) list.toArray(new String[list.size()]);
	}

	public ArrayList<String> getWordsbyArrayList(String str, Context context) {
		ArrayList<String> list = new ArrayList<String>();
		String subString = null;
		int max = 4;
		int i;
		int point = str.length();
		while (point != 0) {
			if (point - max < 0)
				subString = str.substring(0, point);
			else
				subString = str.substring(point - max, point);
			for (i = 0; i < subString.length(); i++) {
				String tempString = subString.substring(i, subString.length());
				if (isInDicByDatabase(tempString, context)
						|| i == subString.length() - 1) {
					list.add(tempString);
					point = point - subString.length() + i;
					break;
				}
			}
		}
		return list;
	}

	public boolean isInDicByDatabase(String source, Context context) {
		long ftime = Calendar.getInstance().getTimeInMillis();// ��ʼʱ��
		String raw = "select dic from dic where dic=\'" + source + "\'";
		Cursor cursor = db.rawQuery(raw, null);
		if (cursor.moveToNext()) {
			// Log.i(source, "�ҵ�");
			Log.i("SQL����ʱ��", Calendar.getInstance().getTimeInMillis() - ftime
					+ "");
			return true;
		} else {
			// Log.i(source, "û�ҵ�");
			Log.i("SQL����ʱ��", Calendar.getInstance().getTimeInMillis() - ftime
					+ "");
			return false;
		}

	}

	public void dbClose() {
		db.close();
	}
}
