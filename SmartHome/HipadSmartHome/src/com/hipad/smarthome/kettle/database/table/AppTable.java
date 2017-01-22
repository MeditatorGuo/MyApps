package com.hipad.smarthome.kettle.database.table;

import android.database.sqlite.SQLiteDatabase;

public abstract class AppTable {

	protected String mTableName;

	/**
	 * ������
	 * 
	 * @param db
	 * @return
	 */
	public abstract void createTable(SQLiteDatabase db);

	/**
	 * ��ʼ��������
	 * 
	 * @param db
	 * @return
	 */
	public abstract void initTable(SQLiteDatabase db);

	/**
	 * ����������
	 * 
	 * @param db
	 * @return
	 */
	public abstract void createIndex(SQLiteDatabase db);

	/**
	 * ����������
	 * 
	 * @param db
	 * @return
	 */
	public abstract void createTrigger(SQLiteDatabase db);

	/**
	 * ɾ����
	 * 
	 * @param db
	 * @return
	 */
	public void dropTable(SQLiteDatabase db) {
		if (mTableName != null) {
			db.execSQL("DROP TABLE IF EXISTS " + mTableName);
		}
	}

	/**
	 * �޸ı�
	 * 
	 * @param db
	 * @return
	 */
	public void alertTable(SQLiteDatabase db) {
		dropTable(db);
		createTable(db);
		createIndex(db);
		createTrigger(db);
		initTable(db);
	}

}
