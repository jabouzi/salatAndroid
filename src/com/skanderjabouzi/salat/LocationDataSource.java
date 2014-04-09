package com.skanderjabouzi.salat;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LocationDataSource {

	// Database fields
	/*private SQLiteDatabase database;
	private DBHelper dbHelper;
	private String[] allColumns = { DBHelper.COLUMN_ID,
			DBHelper.COLUMN_COMMENT };

	public LocationDataSource(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Location createLocation(String location) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_COMMENT, location);
		long insertId = database.insert(DBHelper.TABLE_COMMENTS, null,
				values);
		Cursor cursor = database.query(DBHelper.TABLE_COMMENTS,
				allColumns, DBHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Location newLocation = cursorToLocation(cursor);
		cursor.close();
		return newLocation;
	}

	public void deleteLocation(Location location) {
		long id = location.getId();
		System.out.println("Location deleted with id: " + id);
		database.delete(DBHelper.TABLE_COMMENTS, DBHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Location> getAllLocations() {
		List<Location> locations = new ArrayList<Location>();

		Cursor cursor = database.query(DBHelper.TABLE_COMMENTS,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Location location = cursorToLocation(cursor);
			locations.add(location);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return locations;
	}

	private Location cursorToLocation(Cursor cursor) {
		Location location = new Location();
		location.setId(cursor.getLong(0));
		location.setLocation(cursor.getString(1));
		return location;
	}*/
}
