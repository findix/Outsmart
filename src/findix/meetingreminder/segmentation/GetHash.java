package findix.meetingreminder.segmentation;

import java.io.*;
import java.util.*;

public class GetHash {
	public GetHash() {
	}

	public int[] getHashCode(String str) {
		int HashCode[] = new int[3];
		HashCode[0] = (int) hashBrief(DJBHash(str), 5000011);
		HashCode[1] = (int) hashBrief(DEKHash(str), 5000011);
		HashCode[2] = (int) hashBrief(RSHash(str), 5000011);
		return HashCode;
	}

	@SuppressWarnings("resource")
	public void getHashTable(int Mode) {
		String str;
		File main2012 = new File(
				"C:\\Users\\feng\\workspace\\Bloom\\main2012-5.txt");
		// File RSHash=new
		// File("C:\\Users\\feng\\workspace\\Bloom\\RSHash.txt");
		// File BKDRHash=new
		// File("C:\\Users\\feng\\workspace\\Bloom\\BKDRHash.txt");
		// File SDBMHash=new
		// File("C:\\Users\\feng\\workspace\\Bloom\\SDBMHash.txt");
		// File DJBHash=new
		// File("C:\\Users\\feng\\workspace\\Bloom\\DJBHash.txt");
		// File DEKHash=new
		// File("C:\\Users\\feng\\workspace\\Bloom\\DEKHash.txt");
		File MKHash = new File("C:\\Users\\feng\\workspace\\Bloom\\MKHash.txt");
		try {
			// RSHash.createNewFile();
			// System.out.println("����RS");
			// BKDRHash.createNewFile();
			// System.out.println("����BKDR");
			// SDBMHash.createNewFile();
			// System.out.println("����SDMB");
			// DJBHash.createNewFile();
			// System.out.println("����DJB");
			// DEKHash.createNewFile();
			// System.out.println("����DEK");
			BufferedReader br = new BufferedReader(new FileReader(main2012));
			// BufferedWriter RS=new BufferedWriter(new FileWriter(RSHash));
			// BufferedWriter BKDR=new BufferedWriter(new FileWriter(BKDRHash));
			// BufferedWriter SDBM=new BufferedWriter(new FileWriter(SDBMHash));
			// BufferedWriter DJB=new BufferedWriter(new FileWriter(DJBHash));
			// BufferedWriter DEK=new BufferedWriter(new FileWriter(DEKHash));
			BufferedWriter MK = new BufferedWriter(new FileWriter(MKHash));
			while ((str = br.readLine()) != null) {
				// RS.write(RSHash(str)+"");
				// RS.newLine();
				// BKDR.write(BKDRHash(str)+"");
				// BKDR.newLine();
				// SDBM.write(SDBMHash(str)+"");
				// SDBM.newLine();
				// DJB.write(DJBHash(str)+"");
				// DJB.newLine();
				// DEK.write(DEKHash(str)+"");
				// DEK.newLine();
				MK.write(DEKHash(str) + "");
				MK.newLine();
			}
			// System.out.println("д��ɹ�");
			// br.close();
			// System.out.println("�ر�br");
			// RS.close();
			// System.out.println("�ر�RS");
			// BKDR.close();
			// System.out.println("�ر�BKDR");
			// SDBM.close();
			// System.out.println("�ر�SDBM");
			// DJB.close();
			// System.out.println("�ر�DJB");
			// DEK.close();
			// System.out.println("�ر�DEK");
			MK.close();
			System.out.println("�ر�MK");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double testHash(int mode) {
		double p;
		String str;
		ArrayList<Long> hashTable = new ArrayList<Long>();
		File main2012 = new File(
				"C:\\Users\\feng\\workspace\\Bloom\\main2012-5.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(main2012));
			// for (int i = 0; i < 200000; i++) {
			// if (i % 1000 == 0) {
			// for (int j = 0; j < 4; j++) {
			// str = br.readLine();
			// hashTable.add(hashMode(str, mode));
			// }
			// } else
			// br.readLine();
			// }
			while ((str = br.readLine()) != null) {
				hashTable.add(hashMode(str, mode));
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int odd = 0;
		for (int i = 0; i < hashTable.size(); i++) {
			while ((odd = hashTable.indexOf(hashTable.get(i))) != -1
					&& odd != i) {
				hashTable.remove(odd);
			}
		}
		System.out.println(hashTable.size());
		p = (275714.0 - hashTable.size()) / 275714;
		return p;
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
