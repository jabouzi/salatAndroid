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

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
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
    
    public void addListenerOnButton() {
		latitude = (EditText) findViewById(R.id.latitude);
		longitude = (EditText) findViewById(R.id.longitude);
		timezone = (EditText) findViewById(R.id.timezone);
		city = (EditText) findViewById(R.id.city);
		country = (EditText) findViewById(R.id.country);
		
		btnSaveLocation = (Button) findViewById(R.id.saveLocation);

		btnSaveLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(LocationActivity.this,
						"OnClickListener : " + 
						"\nEditText 1 : " + latitude.getText().toString() +
						"\nEditText 2 : " + longitude.getText().toString() +
						"\nEditText 3 : " + timezone.getText().toString() +
						"\nEditText 4 : " + city.getText().toString() +
						"\nEditText 5 : " + country.getText().toString(),
						Toast.LENGTH_SHORT).show();
			}

		});

	}

}
