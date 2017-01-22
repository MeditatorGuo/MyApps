package com.example.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHelper {

	/** ���ڹ���Ͳ���SQLite���ݿ� */
	private SQLiteDatabase database = null;
	/** ��SQLiteOpenHelper�̳й���������ʵ�����ݿ�Ľ�������� */
	private MySQLiteOpenHelper dbHelper = null;
	/** Ҫ���������ݿ����� */
	private static final String DB_NAME = "mytea.db3";
	/** ���ݿ�汾 */
	private static final int VERSION = 1;
	/** �������� */
	private static final String SQL_CREATE_TABLE = "CREATE TABLE tb_teacontents(_id INTEGER PRIMARY KEY , title , source , create_time , type)";

	public SQLiteDatabaseHelper(Context context) {

		dbHelper = new MySQLiteOpenHelper(context, DB_NAME, null, VERSION);

		database = dbHelper.getReadableDatabase();

		database = dbHelper.getWritableDatabase();

	}

	class MySQLiteOpenHelper extends SQLiteOpenHelper {

		public MySQLiteOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(SQL_CREATE_TABLE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			if (newVersion > oldVersion) {

				db.execSQL("DROP TABLE IF EXISTS tb_teacontents");

				onCreate(db);
			}

		}

	}

	/**
	 * ��ѯ���ݷ���Cursor
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public Cursor selectCursor(String sql, String[] selectionArgs) {

		return database.rawQuery(sql, selectionArgs);

	}

	/**
	 * ����ѯ�õ���Cursor�е�����ת��ΪList����
	 * 
	 * @param cursor
	 * @return
	 */

	private List<Map<String, String>> cursorToList(Cursor cursor) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String[] arrColumnName = cursor.getColumnNames();
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < arrColumnName.length; i++) {
				String cols_value = cursor.getString(i);
				map.put(arrColumnName[i], cols_value);
			}
			list.add(map);
		}
		if (cursor != null) {

			cursor.close();
		}
		return list;
	}

	/**
	 * ִ�д�ռλ����select��䣬����list����
	 * 
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */

	public List<Map<String, String>> SelectData(String sql,
			String[] selectionArgs) {

		Cursor cursor = selectCursor(sql, selectionArgs);

		return cursorToList(cursor);
	}

	/**
	 * ִ�д�ռλ����update��insert��delete��䣬�������ݿ⣬����true��false
	 * 
	 * @param sql
	 * @param bindArgs
	 *            ռλ���Ĳ���ֵ
	 * @return
	 */
	public boolean updataData(String sql, Object[] bindArgs) {
		try {
			if (bindArgs == null) {
				database.execSQL(sql);
			} else {
				database.execSQL(sql, bindArgs);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �ر����ݿ������
	 */
	public void destroy() {
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
		if (database != null) {
			database.close();
			database = null;
		}
	}

}
