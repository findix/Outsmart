package com.find1x.outsmart.segmentation;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class SegmentationByHash {
	HashSet<String> hash = new HashSet<String>();

	public SegmentationByHash() {
		long ftime = Calendar.getInstance().getTimeInMillis();// ��ʼʱ��
		hash = GetDic.getHash();
		Log.i("����hashsetʱ��", Calendar.getInstance().getTimeInMillis() - ftime
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
				if (isInDicByHash(tempString) || i == subString.length() - 1) {
					list.add(tempString);
					point = point - subString.length() + i;
					break;
				}
			}
		}
		return list;
	}

	public boolean isInDicByHash(String source) {
		if (hash.contains((source))) {
			// Log.i(source, "�ҵ�");
			return true;
		} else {
			// Log.i(source, "û�ҵ�");
			return false;
		}
	}

}
