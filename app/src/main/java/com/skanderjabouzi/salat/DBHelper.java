package com.skanderjabouzi.salat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "salat.db";
	private static final int DATABASE_VERSION = 1;

	private static final String OPTIONS_CREATE =
	"CREATE TABLE options (id integer, method integer, asr integer, hijri integer, higherLatitude integer, adhan integer, autoLocation integer); ";
	private static final String LOCATION_CREATE =	
	"CREATE TABLE location (id integer, latitude float, longitude float, country string, city string, timezone float); ";
	Context dBcontext;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		dBcontext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String setApp = dBcontext.getString(R.string.cityCountry).replaceAll("'","''");
		database.execSQL(OPTIONS_CREATE);
		database.execSQL(LOCATION_CREATE);
		database.execSQL(" INSERT INTO options VALUES ('1','1','1','1','1','1','1'); ");
		database.execSQL(" INSERT INTO location VALUES ('1','0','0','"+setApp+"','"+setApp+"','0'); ");
		Log.i("DBHelper", "DB Created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");

		//dBcontext.deleteDatabase("salat.db");
		//sdb.execSQL("DROP TABLE IF EXISTS options; DROP TABLE IF EXISTS location;");
		onCreate(db);
	}
}
