package com.skanderjabouzi.salat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "salat.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE =
	" CREATE TABLE options (id integer, method integer, fajr integer, duhr integer, asr integer, hijri integer, higherLatitude integer); " +
	" CREATE TABLE location (id integer, latitude float, longitude float, country string, city string, timezone integer); " +
	" INSERT INTO options VALUES ('1','0','0','0','0','0','0'); " +
	" INSERT INTO location VALUES ('1','0','0','0','0','0'); ";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS options; DROP TABLE IF EXISTS options;");
		onCreate(db);
	}
}
