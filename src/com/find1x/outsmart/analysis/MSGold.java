package com.find1x.outsmart.analysis;

import java.util.*;

/* 	getTime()Ϊ��ȡʱ��ĺ���,����һ��dateֵ
 * 	getLocation()��ȡλ�ã�����String
 * 	��2�����캯�� һ������StringΪ������ ��һ���޲�����ͨ��setMsg����ΪString �ı���ŵ�ֵ
 *	Warning��isMeeting ����Ҫ��getTime��getLocation֮��ʹ��
 */

public class MSGold {
	String msg = "";

	public MSGold(String msg) {
		this.msg = msg;
	}

	MSGold() {
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isMeeting() {
		return isMeeting;
	}

	public Date getTime() {
		Date time = new Date();
		time.setSeconds(0);
		/*
		 * //���� ���� ���� ����� for(int i=0;i<msg.length();i++){ if(msg.charAt(i)=='��'
		 * && msg.charAt(i+1)=='��'){ //time.setDate(time.getDate()+1);
		 * isMeeting=true; break; } if(msg.charAt(i)=='��' &&
		 * msg.charAt(i+1)=='��'){ time.setDate(time.getDate()+1); break; }
		 * if(msg.charAt(i)=='��' && msg.charAt(i+1)=='��'){
		 * time.setDate(time.getDate()+2); break; } if(msg.charAt(i)=='��' &&
		 * msg.charAt(i+1)=='��' && msg.charAt(i+2)=='��'){
		 * time.setDate(time.getDate()+3); break; } }
		 */// ������timeFixed();

		// xxxx��/xx�� XX��XX��
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == '��') {
				if ((msg.charAt(i - 1) >= '0' && msg.charAt(i - 1) <= '9')
						&& (msg.charAt(i - 2) >= '0' && msg.charAt(i - 2) <= '9')
						&& (msg.charAt(i - 2) >= '0' && msg.charAt(i - 2) <= '9')
						&& (msg.charAt(i - 2) >= '0' && msg.charAt(i - 2) <= '9')) {
					time.setYear(Integer.valueOf(msg.substring(i - 4, i)));
				} else if ((msg.charAt(i - 1) >= '0' && msg.charAt(i - 1) <= '9')
						&& (msg.charAt(i - 2) >= '0' && msg.charAt(i - 2) <= '9')) {
					time.setYear(Integer.valueOf(msg.substring(i - 2, i)));
				}
			}
			if (msg.charAt(i) == '��') {
				if ((msg.charAt(i - 1) >= '0' && msg.charAt(i - 1) <= '9')
						&& (msg.charAt(i - 2) >= '0' && msg.charAt(i - 2) <= '9')) {
					time.setMonth(Integer.valueOf(msg.substring(i - 2, i)) - 1);
				} else if (msg.charAt(i - 1) >= '0' && msg.charAt(i - 1) <= '9') {
					time.setMonth(Integer.valueOf(msg.substring(i - 1, i)) - 1);
				}
			}
			if (msg.charAt(i) == '��' || msg.charAt(i) == '��') {
				if ((msg.charAt(i - 1) >= '0' && msg.charAt(i - 1) <= '9')
						&& (msg.charAt(i - 2) >= '0' && msg.charAt(i - 2) <= '9')) {
					time.setDate(Integer.valueOf(msg.substring(i - 2, i)));
				} else if (msg.charAt(i - 1) >= '0' && msg.charAt(i - 1) <= '9') {
					time.setDate(Integer.valueOf(msg.substring(i - 1, i)));
				}
			}
		}
		// X��/��X
		for (int i = 0; i < msg.length(); i++) {
			if (msg.charAt(i) == ':' || msg.charAt(i) == '��'
					|| msg.charAt(i) == '��') {
				if ((msg.charAt(i - 1) >= '0' && msg.charAt(i - 1) <= '9')
						&& (msg.charAt(i - 2) >= '0' && msg.charAt(i - 2) <= '9')) {
					time.setHours(Integer.valueOf(msg.substring(i - 2, i)));
					timeFixed(i, time);
				} else if (msg.charAt(i - 1) >= '0' && msg.charAt(i - 1) <= '9') {
					time.setHours(Integer.valueOf(msg.substring(i - 1, i)));
					// System.out.println(Integer.valueOf(msg.substring(i-1,i))+12);
					timeFixed(i, time);
				}
				if ((msg.charAt(i + 1) >= '0' && msg.charAt(i + 1) <= '9')
						&& (msg.charAt(i + 2) >= '0' && msg.charAt(i + 2) <= '9')) {
					time.setMinutes(Integer.valueOf(msg.substring(i + 1, i + 3)));
				} else
					time.setMinutes(0);
			}
		}
		/*
		 * //����\����X��X for(int i=0;i<msg.length();i++){ if((msg.charAt(i)=='��' &&
		 * msg.charAt(i+1)=='��') || (msg.charAt(i)=='��' &&
		 * msg.charAt(i+1)=='��')){ if((msg.charAt(i+2)>='0' &&
		 * msg.charAt(i+2)<='9')&&(msg.charAt(i+3)>='0' &&
		 * msg.charAt(i+3)<='9')){
		 * time.setHours(Integer.valueOf(msg.substring(i+2,i+4))); }else
		 * if(msg.charAt(i+2)>='0' && msg.charAt(i+2)<='9'){
		 * time.setHours(Integer.valueOf(msg.substring(i+2,i+3))); }
		 * if(msg.charAt(i+2)=='��'||msg.charAt(i+3)=='��'){
		 * if((msg.charAt(i+1)>='0' &&
		 * msg.charAt(i+1)<='9')&&(msg.charAt(i+2)>='0' &&
		 * msg.charAt(i+2)<='9')){
		 * time.setMinutes(Integer.valueOf(msg.substring(i+1,i+3))); }else{
		 * time.setMinutes(0); } } } }
		 */
		if (time.after(new Date())) {
			isMeeting = true;
		}
		return time;
	}

	public String getLocation() {
		String location = "";
		for (int i = 0; i < msg.length(); i++) {
			for (int j = 0; j < 2; j++) {
				if (msg.charAt(i) == LocationEasy[j]) {
					if ((msg.charAt(i + 1) >= '0' && msg.charAt(i + 1) <= '9')
							&& (msg.charAt(i + 2) >= '0' && msg.charAt(i + 2) <= '9')
							&& (msg.charAt(i + 3) >= '0' && msg.charAt(i + 3) <= '9')
							&& (msg.charAt(i + 4) >= '0' && msg.charAt(i + 4) <= '9')) {
						location = msg.substring(i, i + 5);
					}
				}
			}
		}
		for (int i = 0; i < LocationComplex.length; i++) {
			if (msg.indexOf(LocationComplex[i]) != -1) {
				location = LocationComplex[i];
			}
		}
		if (!location.equals("")) {
			isMeeting = true;
		}
		return location;
	}

	// ���� ���� ��ҹ ��ҹ
	private void timeFixed(int n, Date time) {
		if (time.getHours() < 12) {
			for (int i = n - 1; i >= 0; i--) {
				if (msg.charAt(i) == '��' && msg.charAt(i - 1) == '��') {
					time.setHours(time.getHours() + 12);
					break;
				}
				if (msg.charAt(i) == '��' && msg.charAt(i - 1) == '��') {
					time.setHours(time.getHours() + 12);
					break;
				}
				if (msg.charAt(i) == 'ҹ' && msg.charAt(i - 1) == '��') {
					time.setHours(time.getHours() + 12);
					break;
				}
				if (msg.charAt(i) == 'ҹ' && msg.charAt(i - 1) == '��') {
					time.setHours(time.getHours() + 12);
					break;
				}
			}
		}

		for (int i = n - 1; i > 0; i--) {
			if (msg.charAt(i) == '��' && msg.charAt(i - 1) == '��') {
				time.setDate(time.getDate() + 1);
				break;
			}
			if (msg.charAt(i) == '��' && msg.charAt(i - 1) == '��') {
				time.setDate(time.getDate() + 1);
				if (time.getHours() < 12) {
					time.setHours(time.getHours() + 12);
				}
				break;
			}
			if (msg.charAt(i - 1) == '��' && msg.charAt(i) == '��') {
				// time.setDate(time.getDate()+1);
				break;
			}
			if (msg.charAt(i - 1) == '��' && msg.charAt(i) == '��') {
				time.setDate(time.getDate() + 1);
				break;
			}
			if (msg.charAt(i - 1) == '��' && msg.charAt(i) == '��') {
				time.setDate(time.getDate() + 2);
				break;
			}
			if (i >= 2) {
				if (msg.charAt(i - 2) == '��' && msg.charAt(i - 1) == '��'
						&& msg.charAt(i) == '��') {
					time.setDate(time.getDate() + 3);
					break;
				}
			}
		}

	}

	private boolean isMeeting = false;
	private final char[] LocationEasy = { '��', '��' };
	private final String[] LocationComplex = { "֪��¥", "����¥", "�ܽ�¥", "�˼¥",
			"��������", "�ϲٳ�", "��ͼ", "��ͼ", "��ͼ���", "��ͼ���", "��ʳ��", "��ʳ��", "��ʳ��",
			"һʳ��", "�������յ�վ", "������", "Ʒ����", "��ѧ�������", "38��401c", "���",
			"����Ա�칫��", "��Ա֮��" };
}
