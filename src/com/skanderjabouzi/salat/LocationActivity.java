package com.skanderjabouzi.salat;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
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


public class LocationActivity extends PreferenceActivity implements LocationListener{
    
    private LocationManager locationManager;
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
        final AlertDialog.Builder alert  = new AlertDialog.Builder(this);
        alert.setCancelable(true);
        button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                List<String> providers = locationManager.getAllProviders();
                for (String provider : providers) {
                    printProvider(provider);
                }

                Criteria criteria = new Criteria();
                bestProvider = locationManager.getBestProvider(criteria, false);
                Toast.makeText( getApplicationContext(),"BEST Provider: "+bestProvider,Toast.LENGTH_SHORT).show();
                Location location = locationManager.getLastKnownLocation(bestProvider);
                
                //Toast.makeText( getApplicationContext(),Double.toString(location.getLatitude()),Toast.LENGTH_SHORT).show();  
                if (location == null) 
                {   
                    alert.setButton("OK", new DialogInterface.OnClickListener() 
                    {
                        public void onClick(DialogInterface dialog, int which) 
                        {
                            //alert.close();                 
                        }                 
                    });
                    alert.setTitle("Error");
                    alert.setMessage("Please connect to the internet or set options manually.");
                    alert.show();
                }
                else
                {
                    editor.putString("latitude", "37.55");                    
                    editor.putString("longitude", "55.55");
                    editor.putString("timezone", "-4");
                    editor.putString("city", "Montreal");
                    editor.putString("country", "Canada");
                    prefLatitude.setText( "37.55");
                    prefLongitude.setText( "55.55");
                    prefTimezone.setText( "-4");
                    prefCity.setText( "Montreal");
                    prefCountry.setText( "Canada");
                    editor.commit();
                    
                    salatOptions.getString("latitude", null);
                    salatOptions.getString("timezone", null);
                    //Toast.makeText( getApplicationContext(),salatOptions.getString("longitude", "111"),Toast.LENGTH_SHORT).show(); 
                    //latitude.setText(Double.toString(location.getLatitude()),TextView.BufferType.EDITABLE);
                    //longitude.setText(Double.toString(location.getLongitude()),TextView.BufferType.EDITABLE);
                    TimeZone tz = TimeZone.getDefault();
                    //timezone.setText(Integer.toString(tz.getRawOffset()/3600*1000+tz.getDSTSavings()/3600*1000));
                    Geocoder gcd = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //city.setText(addresses.get(0).getLocality());
                        //country.setText(addresses.get(0).getCountryName());
                    } catch (IOException e) {   }                    
                    cleanLocation();
                }                       
            }
        });        

    }
    
    public void onLocationChanged(Location location) {

    }

    public void onProviderDisabled(String provider) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    
    private void printProvider(String provider) {
        LocationProvider info = locationManager.getProvider(provider);
    }

    private void cleanLocation() {
        locationManager.removeUpdates(this);
    }
}
