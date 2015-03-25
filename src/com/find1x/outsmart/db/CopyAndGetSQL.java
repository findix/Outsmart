package com.find1x.outsmart.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

//��������ڵ���assets�е��ֵ䵽�ֻ���
public class CopyAndGetSQL {

	SQLiteDatabase database;

	public SQLiteDatabase openDatabase(Context context) {
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
		File file = new File(dir, "dic2012.db3");
		//System.out.println("filePath:" + dir);
		// �鿴���ݿ��ļ��Ƿ����
		if (file.exists()) {
			// ������ֱ�ӷ��ش򿪵����ݿ�
			return SQLiteDatabase.openOrCreateDatabase(file, null);
		} else {
			try {
				// �õ����ݿ��������
				InputStream is = context.getResources().getAssets()
						.open("dic2012.db3");
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
				return null;
			}
			// ���û��������ݿ� �����Ѿ�����д���ֻ����ˣ�Ȼ����ִ��һ��������� �Ϳ��Է������ݿ���
			return openDatabase(context);
		}
	}
}