package com.find1x.outsmart.segmentation;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyDic {
	public CopyDic(Context context) {
		//long ftime = Calendar.getInstance().getTimeInMillis();// ��ʼʱ��
		final String FILE_NAME = "dic2012.db";
		File dir = new File("data/data/" + context.getPackageName()
				+ "/databases");
		// �������ȴ����ļ���
		if (!dir.exists() || !dir.isDirectory()) {
			if (dir.mkdir()) {
				System.out.println("�����ɹ�");
			} else {
				System.out.println("����ʧ��");
			}
		}
		File file = new File(dir, FILE_NAME);
		// System.out.println("filePath:" + dir);
		// �鿴�ļ��Ƿ����
		if (!file.exists()) {
			try {
				// �õ�������
				InputStream is = context.getResources().getAssets()
						.open(FILE_NAME);
				// �������д���ֻ���
				FileOutputStream fos = new FileOutputStream(file);
				// ����byte���� ����1KBдһ��
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				// ���ر�
				fos.flush();
				fos.close();
				is.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Log.i("Time of Copy Dic", Calendar.getInstance().getTimeInMillis() - ftime + "");
	}
}
