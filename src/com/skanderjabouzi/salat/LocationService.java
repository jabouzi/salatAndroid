package com.skanderjabouzi.salat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
//import android.app.Activity;
//import android.app.AlertDialog;
import android.os.Bundle;
//import android.preference.PreferenceActivity;
//import android.preference.PreferenceManager;
//import android.preference.EditTextPreference;
//import android.content.SharedPreferences;
//import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
//import android.widget.Button;
//import android.widget.Toast;
//import android.view.View;
//import android.view.View.OnKeyListener;
import android.location.Geocoder;
import android.location.Address;
import java.util.TimeZone;
import java.io.IOException;
import android.app.Service;
import java.util.List;


public class LocationService extends Service implements LocationListener{

	private static final String TAG = "LocationService";
    private LocationManager locationManager;
    private String bestProvider;
    private final Context context = LocationService.this;
    
    @Override
     public IBinder onBind(Intent arg0) {
		return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "start" );
		locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
		List<String> providers = locationManager.getAllProviders();
		//for (String provider : providers) {
			//printProvider(provider);
		//}
		Log.i(TAG, "start" );
		Criteria criteria = new Criteria();
		bestProvider = locationManager.getBestProvider(criteria, false);
		Log.i("BEST Provider: ", bestProvider);
		Location location = locationManager.getLastKnownLocation(bestProvider);

		//Toast.makeText( getApplicationContext(),Double.toString(location.getLatitude()),Toast.LENGTH_SHORT).show();
		if (location == null)
		{
			Log.i(TAG, "NULL" );
			//alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
			//{
				//public void onClick(DialogInterface dialog, int which)
				//{
					//dialog.cancel();
				//}
			//});
			//alert.setTitle("Error");
			//alert.setMessage("Please connect to the internet or set options manually or try again.");
			//alert.show();
		}
		else
		{
			Log.i("LocationService ", String.valueOf(location.getLatitude()));
			//editor.putString("latitude", Double.toString(location.getLatitude()) );
			//editor.putString("longitude", Double.toString(location.getLongitude()) );
			//prefLatitude.setText( Double.toString(location.getLatitude()) );
			//prefLongitude.setText( Double.toString(location.getLongitude()) );

			TimeZone tz = TimeZone.getDefault();
			//editor.putString("timezone", Integer.toString((tz.getRawOffset()/3600*1000+tz.getDSTSavings()/3600*1000)/1000000) );
			//prefTimezone.setText( Integer.toString((tz.getRawOffset()/3600*1000+tz.getDSTSavings()/3600*1000)/1000000) );

			//timezone.setText(Integer.toString(tz.getRawOffset()/3600*1000+tz.getDSTSavings()/3600*1000));
			Geocoder gcd = new Geocoder(getApplicationContext());
			try {
				List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				//Toast.makeText( getApplicationContext(), Double.toString(location.getLatitude()) + " " 
				//+ Double.toString(location.getLongitude())  + " " 
				//+ Double.toString((tz.getRawOffset()/3600*1000+tz.getDSTSavings()/3600*1000)/1000000) + " " 
				//+ addresses.get(0).getLocality()  + " " 
				//+ addresses.get(0).getCountryName()   + " " 
				//,Toast.LENGTH_SHORT).show();
			} catch (IOException e) {   }
			//editor.commit();
			cleanLocation();
		}
        //return null;
        stopService();
    }
    
    @Override
    public void onDestroy() 
    {
		Log.i(TAG,"destroy");
        super.onDestroy();
    }
    
    private void stopService()
    {
		Log.i(TAG,"stop");
        stopService(new Intent(this, LocationService.class));
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
