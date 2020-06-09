package com.hybunion.yirongma.payment.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	public static final String TAG = "SQLiteDemo";
	public static final String DB_NAME = "logindb.db";
	public static final int VERSION = 1;


	public DBHelper(Context context, String name) {
		//super(context, DB_NAME, null, VERSION);
		super(context, DB_NAME, null, VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Database on create");
		//收入表
		String sqlCategoryIncome = "CREATE TABLE loginTable" +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, uid VARCHAR(256), uname VARCHAR(256), upswd VARCHAR(256))";
		db.execSQL(sqlCategoryIncome);
//		insertMessage(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Database on upgrade old version " + oldVersion + ", new version " + newVersion);
	}

	private void insertMessage(SQLiteDatabase db) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("uid", "A1");
		contentValues.put("uname", "11");
		contentValues.put("upswd", "111");
		// 调用insert()方法插入数据
		long result_msg = db.insert("loginTable", null, contentValues);

		if (result_msg != -1) {
		} else {
		}

	}
}
