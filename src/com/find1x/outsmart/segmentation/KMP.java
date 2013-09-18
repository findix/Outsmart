package com.find1x.outsmart.segmentation;

import android.util.Log;

/**
 * Javaʵ��KMP�㷨
 * 
 * ˼�룺ÿ��һ��ƥ������г����ַ��Ƚϲ��ȣ�����Ҫ����iָ�룬 ���������Ѿ��õ��ġ�����ƥ�䡱�Ľ����ģʽ���ҡ�������������Զ ��һ�ξ���󣬼������бȽϡ�
 * 
 * ʱ�临�Ӷ�O(n+m)
 * 
 * 
 * 
 */
public class KMP {

	/**
	 * ����ַ�����next����ֵ
	 * 
	 * @param t
	 *            �ַ���
	 * @return next����ֵ
	 */
	char[] s;
	char[] t;

	public KMP(String s,String t) {
		this.s=s.toCharArray();
		this.t=t.toCharArray();
	}

	public static int[] next(char[] t) {
		int[] next = new int[t.length];
		next[0] = -1;
		int i = 0;
		int j = -1;
		while (i < t.length - 1) {
			if (j == -1 || t[i] == t[j]) {
				i++;
				j++;
				if (t[i] != t[j]) {
					next[i] = j;
				} else {
					next[i] = next[j];
				}
			} else {
				j = next[j];
			}
		}
		return next;
	}

	/**
	 * KMPƥ���ַ���
	 * 
	 * @param s
	 *            ����
	 * @param t
	 *            ģʽ��
	 * @return ��ƥ��ɹ��������±꣬���򷵻�-1
	 */
	public int KMP_Index() {
		int[] next = next(t);
		int i = 0;
		int j = 0;
		while (i <= s.length - 1 && j <= t.length - 1) {
			if (j == -1 || s[i] == t[j]) {
				i++;
				j++;
			} else {
				j = next[j];
			}
		}
		if (j < t.length) {
			return -1;
		} else
			return i - t.length; // ����ģʽ���������е�ͷ�±�
	}
	public boolean isInDicByString(String source) {
		source = "\n" + source + "\n";
		KMP kmp = new KMP("str", source);
		if (kmp.KMP_Index() != -1) {
			 Log.i(source, "�ҵ�");
			return true;
		} else {
			return false;
		}

	}
}
