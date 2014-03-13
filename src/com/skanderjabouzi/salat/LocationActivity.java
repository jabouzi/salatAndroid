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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnKeyListener;
import android.location.Geocoder;
import android.location.Address;
import java.util.TimeZone;
import java.io.IOException;
import java.util.List;


public class LocationActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{
    
    private LocationManager locationManager;
    private String bestProvider;
    private SharedPreferences salatOptions; 
    private SharedPreferences.Editor editor;

    	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);
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
