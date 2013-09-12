package com.find1x.meetingreminder.segmentation;

public class GetHash {
	public GetHash() {
	}

	public int[] getHashCode(String str) {
		int HashCode[] = new int[3];
		HashCode[0] = (int) hashBrief(hashMode(str,4), 5000011);
		HashCode[1] = (int) hashBrief(hashMode(str,5), 5000011);
		HashCode[2] = (int) hashBrief(hashMode(str,1), 5000011);
		return HashCode;
	}

	private long hashMode(String str, int mode) {
		switch (mode) {
		case 1:
			return RSHash(str);
		case 2:
			return BKDRHash(str);
		case 3:
			return SDBMHash(str);
		case 4:
			return DJBHash(str);
		case 5:
			return DEKHash(str);
		case 6:
			return MKHash(str);
		default:
			return -1;
		}
	}

	// 1
	// ��Robert Sedgwicks�� Algorithms in Cһ���еõ��ˡ�
	// ��(ԭ������)�Ѿ������һЩ�򵥵��Ż����㷨���Լӿ���ɢ�й��̡�
	public long RSHash(String str) {
		int b = 378551;
		int a = 63689;
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = hash * a + str.charAt(i);
			a = a * b;
		}
		return hash;
	}

	// 2
	// ����㷨����Brian Kernighan �� Dennis Ritchie�� The C Programming Language��
	// ����һ���ܼ򵥵Ĺ�ϣ�㷨,ʹ����һϵ����ֵ�����,
	// ��ʽ��31,3131,31...31,����ȥ��DJB�㷨�����ơ�
	public long BKDRHash(String str) {
		long seed = 131; // 31 131 1313 13131 131313 etc..
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash * seed) + str.charAt(i);
		}
		return hash;
	}

	// 3
	// ����㷨�ڿ�Դ��SDBM��ʹ�ã��ƺ��Ժܶ಻ͬ���͵����ݶ��ܵõ�����ķֲ�
	public long SDBMHash(String str) {
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
		}
		return hash;
	}

	// 4
	// ����㷨��Daniel J.Bernstein ���ڷ����ģ���Ŀǰ����������Ч�Ĺ�ϣ������
	public long DJBHash(String str) {
		long hash = 5381;
		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) + hash) + str.charAt(i);
		}
		return hash;
	}

	// 5
	// ��ΰ���Knuth�ڡ���̵����� �������ĵ���������������и�����
	public long DEKHash(String str) {
		long hash = str.length();
		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
		}
		return hash;
	}

	// 6
	// ��ΰ���MK��4��5��϶�����Ĺ�ϣ����
	public long MKHash(String str) {
		long hash = 5381;
		for (int i = 0; i < str.length(); i++) {
			hash = ((hash << 5) + (hash >> 2)) + str.charAt(i);
		}
		return hash;
	}

	// private int Mode = 0;
	public long hashBrief(long hash, int Prime) {
		long Bhash = 0;
		Bhash = hash % Prime;
		return Math.abs(Bhash);
	}
}
