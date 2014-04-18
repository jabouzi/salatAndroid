package com.skanderjabouzi.salat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.widget.Toast;
import android.location.Geocoder;
import android.location.Address;
import java.util.TimeZone;
import java.io.IOException;
import android.app.Service;
import java.util.List;


public class LocationService extends Service implements LocationListener{

    private static final String TAG = "LOCATIONSERVICE";
    public static final String LOCATION_INTENT = "com.skanderjabouzi.salat.LOCATION_INTENT";
    public static final String LOCATION = "LOCATION";
    public static final String RECEIVE_LOCATION_NOTIFICATIONS = "com.skanderjabouzi.salat.RECEIVE_LOCATION_NOTIFICATIONS";
    private LocationManager locationManager;
    private String bestProvider;
    private com.skanderjabouzi.salat.Location myLocation;

     @Override
    public IBinder onBind(Intent arg0) {
		return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myLocation = new com.skanderjabouzi.salat.Location();
        final AlertDialog.Builder alert  = new AlertDialog.Builder(this);
        alert.setCancelable(true);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		bestProvider = locationManager.getBestProvider(criteria, false);
		Log.i(TAG, "BEST Provider: " + bestProvider);
		android.location.Location location = locationManager.getLastKnownLocation(bestProvider);
		Log.i(TAG, "LATITUDE: " + location.getLatitude());
		if (location == null)
		{
			Intent intent;
			intent = new Intent(LOCATION_INTENT);
			intent.putExtra(LOCATION, "NULL");
			sendBroadcast(intent, RECEIVE_LOCATION_NOTIFICATIONS);
			Log.i(TAG, "onHandleIntent #5 " + "LOCATION NULL");
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
			TimeZone tz = TimeZone.getDefault();
			Geocoder gcd = new Geocoder(getApplicationContext());
			try {
				List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				myLocation.setLatitude((float)location.getLatitude());
				myLocation.setLongitude((float)location.getLongitude());
				myLocation.setTimezone(((float)tz.getRawOffset()/3600*1000+tz.getDSTSavings()/3600*1000)/1000000);
				myLocation.setCity(addresses.get(0).getLocality());
				myLocation.setCountry(addresses.get(0).getCountryName());
				
				Intent intent;
				intent = new Intent(LOCATION_INTENT);
				intent.putExtra(LOCATION, myLocation);
				sendBroadcast(intent, RECEIVE_LOCATION_NOTIFICATIONS);
				Log.i(TAG, "onHandleIntent #5 " + "LOCATION" + myLocation.getLatitude());
				
			} catch (IOException e) {   }
			cleanLocation();
			stopService();
		}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    private void stopService()
    {
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
