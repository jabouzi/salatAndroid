package com.skanderjabouzi.salat;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class OptionsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public OptionsDataSource(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	void addOptions(Options options) {
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put('id', options.geId());
		values.put('method', options.geMethod());
		values.put('asr', options.geAsr());
		values.put('hijri', options.getHijri());
		values.put('higherLatitude', options.getHigherLatitude());

		db.insert('options', null, values);
		db.close();
	}

	// Getting single options
	Options getOptions(int id) {
		SQLiteDatabase db = DBHelper.getReadableDatabase();

		Cursor cursor = db.query('options', new String[] { KEY_ID,
				KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Options options = new Options(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return options
		return options;
	}

	// Getting All Options
	public List<Options> getAllOptions() {
		List<Options> optionsList = new ArrayList<Options>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + 'options';

		SQLiteDatabase db = DBHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

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
	}

	// Updating single options
	public int updateOptions(Options options) {
		SQLiteDatabase db = DBHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, options.getName());
		values.put(KEY_PH_NO, options.getPhoneNumber());

		// updating row
		return db.update('options', values, KEY_ID + " = ?",
				new String[] { String.valueOf(options.getID()) });
	}

	// Deleting single options
	public void deleteOptions(Options options) {
		SQLiteDatabase db = DBHelper.getWritableDatabase();
		db.delete('options', KEY_ID + " = ?",
				new String[] { String.valueOf(options.getID()) });
		db.close();
	}


	// Getting options Count
	public int getOptionsCount() {
		String countQuery = "SELECT  * FROM " + 'options';
		SQLiteDatabase db = DBHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
