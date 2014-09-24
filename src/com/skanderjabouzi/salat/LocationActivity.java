package com.skanderjabouzi.salat;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.provider.Settings;

public class LocationActivity extends Activity{

	static final String SEND_LOCATION_NOTIFICATIONS = "com.skanderjabouzi.salat.SEND_LOCATION_NOTIFICATIONS";
	private EditText latitude, longitude, timezone, city, country;
	private Button btnSaveLocation, btnDetectLocation;
	private LocationDataSource datasource;
	private Location location;
	private SalatApplication salatApp;
	private Context context = LocationActivity.this;
	private Intent locationIntent;
    LocationReceiver receiver;
    IntentFilter filter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        receiver = new LocationReceiver();
        filter = new IntentFilter( LocationService.LOCATION_INTENT );
        salatApp = SalatApplication.getInstance(this);
        locationIntent = new Intent(this, LocationService.class);
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
        LocationActivity.this.finish();
        unregisterReceiver(receiver);
        datasource.close();
    }
    
    @Override
    protected void onStop() {
        super.onStop();
		LocationActivity.this.finish();
        //unregisterReceiver(receiver);
        datasource.close();
    }
    
	@Override
    protected void onDestroy() {
        super.onDestroy();
        LocationActivity.this.finish();
        //unregisterReceiver(receiver);
        datasource.close();
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
				Intent athanIntent = new Intent(context, SalatReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, athanIntent, 0);
				AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);
				finish();
			}

		});
		
		btnDetectLocation = (Button) findViewById(R.id.detectLocation);
		btnDetectLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startService(new Intent(context, LocationService.class));
			}

		});

	}
	
	public void showSettingsAlert(int type) {
		
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Localisation error");
        alertDialog.setMessage("Please enable Internet and try again");
        if (type == 1) alertDialog.setMessage("Please enable GPS or Internet and try again");
 
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
 
        alertDialog.show();
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
            String extraString = intent.getStringExtra("LOCATION");
            if (extraString.equals("GEO_NULL"))
            {
				showSettingsAlert(1);
			}
            else if (extraString.equals("LOCATION_NULL"))
            {
				showSettingsAlert(0);
			}
			else
			{
				String[] geolocation = extraString.split("\\|");
				location.setLatitude(Float.parseFloat(geolocation[0]));
				location.setLongitude(Float.parseFloat(geolocation[1]));
				location.setTimezone(Float.parseFloat(geolocation[2]));
				location.setCity(geolocation[3]);
				location.setCountry(geolocation[4]);
				setLocationTexts();
			}
        }
    }
}
