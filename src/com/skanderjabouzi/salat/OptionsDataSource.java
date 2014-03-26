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
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_COMMENT };

	public OptionsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	/*public Options createOptions(String options) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_COMMENT, options);
		long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Options newOptions = cursorToOptions(cursor);
		cursor.close();
		return newOptions;
	}

	public void deleteOptions(Options options) {
		long id = options.getId();
		System.out.println("Options deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Options> getAllOptionss() {
		List<Options> optionss = new ArrayList<Options>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Options options = cursorToOptions(cursor);
			optionss.add(options);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return optionss;
	}

	private Options cursorToOptions(Cursor cursor) {
		Options options = new Options();
		options.setId(cursor.getLong(0));
		options.setOptions(cursor.getString(1));
		return options;
	}*/
}
