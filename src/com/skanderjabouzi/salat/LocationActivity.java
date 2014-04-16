package com.skanderjabouzi.salat;

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

public class LocationActivity extends Activity{

	private EditText latitude, longitude, timezone, city, country;
	private Button btnSaveLocation, btnDetectLocation;
	private LocationDataSource datasource;
	private Location location;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        datasource = new LocationDataSource(this);
		datasource.open();
		location = datasource.getLocation(1);
		setLocationTexts();
		addListenerOnButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    public void setLocationTexts() {
		
		latitude = (EditText) findViewById(R.id.latitude);
		latitude.setText(String.valueOf(location.getLatitude()));
		longitude = (EditText) findViewById(R.id.longitude);
		longitude.setText(String.valueOf(location.getLongitude()));
		timezone = (EditText) findViewById(R.id.timezone);
		timezone.setText(String.valueOf(location.getTimezone()));
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
				//Toast.makeText(LocationActivity.this,
						//"OnClickListener : " +
						//"\nEditText 1 : " + latitude.getText().toString() +
						//"\nEditText 2 : " + longitude.getText().toString() +
						//"\nEditText 3 : " + timezone.getText().toString() +
						//"\nEditText 4 : " + city.getText().toString() +
						//"\nEditText 5 : " + country.getText().toString(),
						//Toast.LENGTH_SHORT).show();
			}

		});

	}

}
