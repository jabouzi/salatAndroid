package com.skanderjabouzi.salat;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OptionsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private DBHelper DBHelper;

	public OptionsDataSource(Context context) {
		DBHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = DBHelper.getWritableDatabase();
		Log.i("OptionsDataSource", "open");
	}

	public void close() {
		DBHelper.close();
	}

	void addOptions(Options options) {
		ContentValues values = new ContentValues();
		values.put("id", options.getId());
		values.put("method", options.getMethod());
		values.put("asr", options.getAsr());
		values.put("hijri", options.getHijri());
		values.put("higherLatitude", options.getHigherLatitude());
		values.put("adhan", options.getAdhan());
		values.put("autoLocation", options.getAutoLocation());

		database.insert("options", null, values);
		database.close();
	}

	// Getting single options
	Options getOptions(int id) {
		Cursor cursor = database.query("options", new String[] { "id",
				"method", "asr", "hijri", "higherLatitude", "adhan", "autoLocation"}," id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) cursor.moveToFirst();

		Options options = new Options();
		options.setId(cursor.getLong(0));
		options.setMethod(cursor.getInt(1));
		options.setAsr(cursor.getInt(2));
		options.setHijri(cursor.getInt(3));
		options.setHigherLatitude(cursor.getInt(4));
		options.setAdhan(cursor.getInt(5));
		options.setAutoLocation(cursor.getInt(6));
		cursor.close();
		
		// return options
		return options;
	}

	// Getting All Options
	/*public List<Options> getAllOptions() {
		List<Options> optionsList = new ArrayList<Options>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + "options";
		Cursor cursor = database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Options options = new Options();
				options.setID(Integer.parseInt(cursor.getString(0)));
				options.setName(cursor.getString(1));
				options.setPhoneNumber(cursor.getString(2));
				// Adding options to list
				optionsList.add(options);
			} while (cursor.moveToNext());
		}

		// return options list
		return optionsList;
	}*/

	// Updating single options
	public int updateOptions(Options options) {
		ContentValues values = new ContentValues();
		values.put("method", options.getMethod());
		values.put("asr", options.getAsr());
		values.put("hijri", options.getHijri());
		values.put("higherLatitude", options.getHigherLatitude());
		values.put("adhan", options.getAdhan());
		values.put("autoLocation", options.getAutoLocation());

		// updating row
		return database.update("options", values," id = ?",
				new String[] { String.valueOf(options.getId()) });
	}

	// Deleting single options
	public void deleteOptions(Options options) {
		database.delete("options"," id = ?",
				new String[] { String.valueOf(options.getId()) });
		database.close();
	}


	// Getting options Count
	public int getOptionsCount() {
		String countQuery = "SELECT  * FROM options";
		Cursor cursor = database.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

}
