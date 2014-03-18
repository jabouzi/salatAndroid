package com.skanderjabouzi.salat;

import android.content.Intent;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.EditTextPreference;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnKeyListener;
//import java.util.TimeZone;
//import java.io.IOException;
//import java.util.List;


public class LocationActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	//private LocationManager locationManager;
	private String bestProvider;
	private SharedPreferences salatOptions;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.location);
		setContentView(R.layout.location);
		salatOptions = PreferenceManager.getDefaultSharedPreferences(this);
		editor = salatOptions.edit();
		final Button button = (Button) findViewById(R.id.locationButton);
		final EditTextPreference prefLatitude = (EditTextPreference) findPreference("latitude");
		final EditTextPreference prefLongitude = (EditTextPreference) findPreference("longitude");
		final EditTextPreference prefTimezone = (EditTextPreference) findPreference("timezone");
		final EditTextPreference prefCity = (EditTextPreference) findPreference("city");
		final EditTextPreference prefCountry = (EditTextPreference) findPreference("country");
		//final AlertDialog.Builder alert  = new AlertDialog.Builder(this);
		//alert.setCancelable(true);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent serviceIntent = new Intent();
				serviceIntent.setAction("com.skanderjabouzi.salat.LocationService");
				startService(serviceIntent);
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
		salatOptions = PreferenceManager.getDefaultSharedPreferences(this);
		salatOptions.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		salatOptions.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		//getActivity().sendBroadcast( new Intent("com.marakana.android.yamba.action.UPDATED_INTERVAL") );
	}


}
