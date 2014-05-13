package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.content.IntentFilter;
import android.app.AlarmManager;
import android.util.Log;

public class LocationActivity extends Activity{

	static final String SEND_LOCATION_NOTIFICATIONS = "com.skanderjabouzi.salat.SEND_LOCATION_NOTIFICATIONS";
	private EditText latitude, longitude, timezone, city, country;
	private Button btnSaveLocation, btnDetectLocation;
	private LocationDataSource datasource;
	private Location location;
	private SalatApplication salatApp;
	private Context context;
	private Intent athanIntent;
	private Intent locationIntent;
	private PendingIntent pendingIntent;
    LocationReceiver receiver;
    IntentFilter filter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        receiver = new LocationReceiver();
        filter = new IntentFilter( LocationService.LOCATION_INTENT );
        salatApp = new SalatApplication(this);
        athanIntent = new Intent(this, SalatReceiver.class);
        locationIntent = new Intent(this, LocationService.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, athanIntent, 0);
        datasource = new LocationDataSource(this);
		datasource.open();
		location = datasource.getLocation(1);
		setLocationTexts();
		addListenerOnButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.registerReceiver(receiver, filter, SEND_LOCATION_NOTIFICATIONS, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void setLocationTexts() {
		
		latitude = (EditText) findViewById(R.id.latitude);
		latitude.setText(fmt(location.getLatitude()));
		longitude = (EditText) findViewById(R.id.longitude);
		longitude.setText(fmt(location.getLongitude()));		
		timezone = (EditText) findViewById(R.id.timezone);
		timezone.setText(fmt(location.getTimezone()));
		city = (EditText) findViewById(R.id.city);
		city.setText(location.getCity());
		country = (EditText) findViewById(R.id.country);
		country.setText(location.getCountry());
	}

    public void addListenerOnButton() {

		btnSaveLocation = (Button) findViewById(R.id.saveLocation);
		btnSaveLocation.setOnClickListener(new OnClickListener() {
		// CALL LOCATION SERVICE
			@Override
			public void onClick(View v) {
		
				location.setId(1);
				latitude = (EditText) findViewById(R.id.latitude);
				location.setLatitude(Float.parseFloat(String.valueOf(latitude.getText())));

				longitude = (EditText) findViewById(R.id.longitude);
				location.setLongitude(Float.parseFloat(String.valueOf(longitude.getText())));

				timezone = (EditText) findViewById(R.id.timezone);
				location.setTimezone(Float.parseFloat(String.valueOf(timezone.getText())));

				city = (EditText) findViewById(R.id.city);
				location.setCity(String.valueOf(city.getText()));

				country = (EditText) findViewById(R.id.country);
				location.setCountry(String.valueOf(country.getText()));

				datasource.updateLocation(location);
				
				long timeToSalat = salatApp.getTimeToSalat();
				AlarmManager alarmManager = (AlarmManager) (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);

				//Log.i("LocationActivity", "Next salat is " + salatApp.nextSalat  + " in " + timeToSalat);
				
				finish();
			}

		});
		btnDetectLocation = (Button) findViewById(R.id.detectLocation);
		btnDetectLocation.setOnClickListener(new OnClickListener() {
		// CALL LOCATION SERVICE
			@Override
			public void onClick(View v) {
				
				//context = getApplicationContext();
				Log.i("LOCATV", "onclick");
				//context.startService(locationIntent);
				startService(new Intent(LocationActivity.this, LocationService.class));
			}

		});

	}
	
	public String fmt(float d)
	{
		if(d == (int) d)
			return String.valueOf((int)d);
		else
			return String.valueOf(d);
	}
	
	class LocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String locationValues = intent.getStringExtra("LOCATION");
            Log.i("LocationReceiver ", locationValues);
            //setLocationTexts();
        }
    }
}
