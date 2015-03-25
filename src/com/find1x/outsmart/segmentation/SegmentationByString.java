package com.find1x.outsmart.segmentation;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class SegmentationByString {
	String str;

	public SegmentationByString() {
		long ftime = Calendar.getInstance().getTimeInMillis();// ��ʼʱ��
		//str = GetDic.getString();
		str = GetDic.getString();
		Log.i("����Stringʱ��", Calendar.getInstance().getTimeInMillis() - ftime
				+ "");// ����ʱ��
	}

	public String[] getWords(String str) {
		long ftime = Calendar.getInstance().getTimeInMillis();
		ArrayList<String> list = getWordsbyArrayList(str);
		Log.i("�ִ�ʱ��", Calendar.getInstance().getTimeInMillis() - ftime + "");// ����ʱ��
		return (String[]) list.toArray(new String[list.size()]);
	}

	public ArrayList<String> getWordsbyArrayList(String str) {
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
				if (isInDicByString(tempString) || i == subString.length() - 1) {
					list.add(tempString);
					point = point - subString.length() + i;
					break;
				}
			}
		}
		return list;
	}

	public boolean isInDicByString(String source) {
		int index;
		index = str.indexOf("\n"+source + "\n");//���ٷִ�
		// index = str.indexOf("\r\n"+source + "\r\n");//�߾�ȷ�ִʣ�����Ч�ʺܵ�
		if (index != -1) {
			// Log.i(source, str.substring(index - 1, index));
			// Log.i(source, "�ҵ�");
			return true;
		} else {
			// Log.i(source, "û�ҵ�");
			return false;
		}
	}
}
