package com.skanderjabouzi.salat;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocationDataSource {

	// Database fields
	private SQLiteDatabase database;
	private DBHelper dbHelper;

	public LocationDataSource(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		Log.i("LocationDataSource", "open");
	}

	public void close() {
		dbHelper.close();
	}

	void addLocation(Location location) {
		ContentValues values = new ContentValues();
		values.put("_id", location.getId());
		values.put("latitude", location.getLatitude());
		values.put("longitude", location.getLongitude());
		values.put("city", location.getCity());
		values.put("country", location.getCountry());
		values.put("timezone", location.getTimezone());

		database.insert("location", null, values);
		database.close();
	}

	// Getting single location
	Location getLocation(int id) {
		Cursor cursor = database.query("location", new String[] { "_id",
				"latitude", "longitude", "city", "country", "timezone"}," id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Location location = new Location();
		location.setId(cursor.getLong(0));
		location.setLatitude(cursor.getInt(1));
		location.setLongitude(cursor.getInt(2));
		location.setCity(cursor.getString(3));
		location.setCountry(cursor.getString(4));
		location.setTimezone(cursor.getInt(5));
		// return location
		return location;
	}

	// Getting All Location
	/*public List<Location> getAllLocation() {
		List<Location> locationList = new ArrayList<Location>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + "location";
		Cursor cursor = database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Location location = new Location();
				location.setID(Integer.parseInt(cursor.getString(0)));
				location.setName(cursor.getString(1));
				location.setPhoneNumber(cursor.getString(2));
				// Adding location to list
				locationList.add(location);
			} while (cursor.moveToNext());
		}

		// return location list
		return locationList;
	}*/

	// Updating single location
	public int updateLocation(Location location) {
		ContentValues values = new ContentValues();
		values.put("latitude", location.getLatitude());
		values.put("longitude", location.getLongitude());
		values.put("city", location.getCity());
		values.put("country", location.getCountry());
		values.put("timezone", location.getTimezone());

		// updating row
		return database.update("location", values," id = ?",
				new String[] { String.valueOf(location.getId()) });
	}

	// Deleting single location
	public void deleteLocation(Location location) {
		database.delete("location"," id = ?",
				new String[] { String.valueOf(location.getId()) });
		database.close();
	}


	// Getting location Count
	public int getLocationCount() {
		String countQuery = "SELECT  * FROM " + "location";
		Cursor cursor = database.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
}
