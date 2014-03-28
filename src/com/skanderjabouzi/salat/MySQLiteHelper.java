package com.skanderjabouzi.salat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "salat.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = 
	" CREATE TABLE calculation (id integer, method integer, fajr integer, duhr integer, asr integer, hijri integer, higherLat integer); " +
	" CREATE TABLE location (id integer, latitude float, longitude float, country string, city string, timezone integer); " +
	" INSERT INTO calculation VALUES ('1','0','0','0','0','0','0'); " +
	" INSERT INTO location VALUES ('1','0','0','0','0','0'); ";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
		onCreate(db);
	}
}
