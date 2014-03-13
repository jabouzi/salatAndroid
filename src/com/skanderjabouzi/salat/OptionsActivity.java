package com.skanderjabouzi.salat;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

public class OptionsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	private SharedPreferences salatOptions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.options);
	}
        
    @Override
	public void onStart() {
		super.onStart();
		salatOptions = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
		getActivity().sendBroadcast( new Intent("com.marakana.android.yamba.action.UPDATED_INTERVAL") );
	}
}
