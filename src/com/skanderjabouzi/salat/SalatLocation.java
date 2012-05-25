package com.skanderjabouzi.salat;

import java.util.List;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.view.View.OnKeyListener;
import java.util.TimeZone;
import java.io.IOException;
import android.location.Geocoder;
import android.location.Address;


public class SalatLocation extends Activity implements LocationListener {
    private static final String TAG = "LocationDemo";
    private static final String[] S = { "Out of Service",
            "Temporarily Unavailable", "Available" };

    private TextView output;
    private LocationManager locationManager;
    private String bestProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        
        final EditText latitude = (EditText) findViewById(R.id.latitude);
        final EditText longitude = (EditText) findViewById(R.id.longitude);
        final EditText timezone = (EditText) findViewById(R.id.timezone);
        final EditText city = (EditText) findViewById(R.id.city);
        final EditText country = (EditText) findViewById(R.id.country);
        final Button button = (Button) findViewById(R.id.button);
        
        button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                // List all providers:
                List<String> providers = locationManager.getAllProviders();
                for (String provider : providers) {
                    printProvider(provider);
                }

                Criteria criteria = new Criteria();
                bestProvider = locationManager.getBestProvider(criteria, false);
                Toast.makeText( getApplicationContext(),"BEST Provider: "+bestProvider,Toast.LENGTH_SHORT).show();
                //output.append("\n\nLocations (starting with last known):");
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location == null) Toast.makeText( getApplicationContext(),"Location[unknown]",Toast.LENGTH_SHORT).show();                   
                else
                {
                    latitude.setText(Double.toString(location.getLatitude()),TextView.BufferType.EDITABLE);
                    longitude.setText(Double.toString(location.getLongitude()),TextView.BufferType.EDITABLE);
                    TimeZone tz = TimeZone.getDefault();
                    timezone.setText(Integer.toString(tz.getRawOffset()/3600*1000+tz.getDSTSavings()/3600*1000));
                    Geocoder gcd = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        city.setText(addresses.get(0).getLocality());
                        country.setText(addresses.get(0).getCountryName());
                    } catch (IOException e) {   }                    
                    printLocation();
                }
                //Toast.makeText( getApplicationContext(),location.toString(),Toast.LENGTH_SHORT).show();
                    //output.append("\n\n" + location.toString());                          
             }
         });        

        // Get the output UI
        //output = (TextView) findViewById(R.id.output);

        // Get the location manager
        
    }

    /** Register for the updates when Activity is in foreground */
    //~ @Override
    //~ protected void onResume() {
        //~ super.onResume();
        //~ locationManager.requestLocationUpdates(bestProvider, 20000, 1, this);
    //~ }

    /** Stop the updates when Activity is paused */
    //~ @Override
    //~ protected void onPause() {
        //~ super.onPause();
        //~ locationManager.removeUpdates(this);
    //~ }

    public void onLocationChanged(Location location) {
        //printLocation(location);
        //locationManager.removeUpdates(this);
    }

    public void onProviderDisabled(String provider) {
        // let okProvider be bestProvider
        // re-register for updates
        //output.append("\n\nProvider Disabled: " + provider);
    }

    public void onProviderEnabled(String provider) {
        // is provider better than bestProvider?
        // is yes, bestProvider = provider
        //output.append("\n\nProvider Enabled: " + provider);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        //output.append("\n\nProvider Status Changed: " + provider + ", Status="
                //+ S[status] + ", Extras=" + extras);
    }

    private void printProvider(String provider) {
        LocationProvider info = locationManager.getProvider(provider);
        //output.append(info.toString() + "\n\n");
    }

    private void printLocation() {
        locationManager.removeUpdates(this);
    }

}
