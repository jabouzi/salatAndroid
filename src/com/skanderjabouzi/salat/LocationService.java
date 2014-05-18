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
import java.util.Locale;
import java.util.TimeZone;
import java.io.IOException;
import android.app.Service;
import java.util.List;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LocationService extends Service implements LocationListener{

	private static final String TAG = "LocationService";
	public static final String LOCATION_INTENT = "com.skanderjabouzi.salat.LOCATION_INTENT";
    public static final String LOCATION = "LOCATION";
    public static final String RECEIVE_LOCATION_NOTIFICATIONS = "com.skanderjabouzi.salat.RECEIVE_LOCATION_NOTIFICATIONS";
    private LocationManager locationManager;
    private String bestProvider;
    private final Context context = LocationService.this;
    boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	double latitude; 
	double longitude; 
	private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "";
	Location location;	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    
    @Override
     public IBinder onBind(Intent arg0) {
		return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "start" );
		getLocation();
		getGeoLocation();
        cleanLocation();
        stopService();
    }
    
    public void getLocation() {
		try {
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				Log.i("PROVIDERS", "NULL" );
				sendNotification("PROVIDERS_NULL");
			} 
			else 
			{
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							Log.d("Location", String.valueOf(location.getLatitude()));
						}
						else
						{
							Log.d("NETWORK", "NULL");
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
							else
							{
								Log.d("GPS", "NULL");
							}

						}
					}
				}
			}
		}		
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void getGeoLocation() {
		if (location == null)
		{
			sendNotification("LOCATION_NULL");
			Log.d("LOCATION", "NULL");
		}
		else
		{
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			TimeZone tz = TimeZone.getDefault();			
			//Geocoder gcd = new Geocoder(context, Locale.getDefault());
			
			JSONObject jsonObj = null;
			String str = "";
			HttpResponse response;
			HttpClient myClient = new DefaultHttpClient();
			HttpPost myConnection = new HttpPost("http://maps.google.com/maps/api/geocode/json?sensor=false&latlng="+latitude+","+longitude);
			try {
				response = myClient.execute(myConnection);
				str = EntityUtils.toString(response.getEntity(), "UTF-8");
				Log.d("RESPONSE ", str);
				jsonObj = new JSONObject(str);
				String Status = jsonObj.getString("status");
				Log.d("RESPONSE ", Status);
				if (Status.equalsIgnoreCase("OK")) {
					JSONArray Results = jsonObj.getJSONArray("results");
					JSONObject zero = Results.getJSONObject(0);
					JSONArray address_components = zero.getJSONArray("address_components");

					for (int i = 0; i < address_components.length(); i++) {
						JSONObject zero2 = address_components.getJSONObject(i);
						String long_name = zero2.getString("long_name");
						JSONArray mtypes = zero2.getJSONArray("types");
						String Type = mtypes.getString(0);                    
						if (Type.equalsIgnoreCase("street_number")) {
							Address1 = long_name + " ";
						} else if (Type.equalsIgnoreCase("route")) {
							Address1 = Address1 + long_name;
						} else if (Type.equalsIgnoreCase("sublocality")) {
							Address2 = long_name;
						} else if (Type.equalsIgnoreCase("locality")) {
							// Address2 = Address2 + long_name + ", ";
							City = long_name;
						} else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
							County = long_name;
						} else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
							State = long_name;
						} else if (Type.equalsIgnoreCase("country")) {
							Country = long_name;
						} else if (Type.equalsIgnoreCase("postal_code")) {
							PIN = long_name;
						}
					}
				}
				
				String locationValues = String.valueOf(location.getLatitude());
				locationValues += "|" + String.valueOf(location.getLongitude());
				locationValues += "|" + String.valueOf((tz.getRawOffset()/3600*1000+tz.getDSTSavings()/3600*1000)/1000000);
				locationValues += "|" + City;
				locationValues += "|" + Country;
				sendNotification(locationValues);
				 
			} catch (ClientProtocolException e) {
				sendNotification("LOCATION_NULL");
			} catch (IOException e) {
				sendNotification("LOCATION_NULL");
			} catch ( JSONException e) {
				e.printStackTrace();                
			}
			

		}
		/*try {
				List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				locationValues += "|" + addresses.get(0).getLocality();
				locationValues += "|" + addresses.get(0).getCountryName();
				
				//Toast.makeText( getApplicationContext(), Double.toString(location.getLatitude()) + " " 
				//+ Double.toString(location.getLongitude())  + " " 
				//+ Double.toString((tz.getRawOffset()/3600*1000+tz.getDSTSavings()/3600*1000)/1000000) + " " 
				//+ addresses.get(0).getLocality()  + " " 
				//+ addresses.get(0).getCountryName()   + " " 
				//,Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			//catch (JSONException e) {
				//e.printStackTrace();
			//}
			
			Log.d("LOCATIONVAL", locationValues);
			sendNotification(locationValues);
		}*/
	}
	
	public void sendNotification(String extra)
	{
		Intent intent;
		intent = new Intent(LOCATION_INTENT);
		intent.putExtra(LOCATION, extra);
		sendBroadcast(intent, RECEIVE_LOCATION_NOTIFICATIONS);
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
		Log.i(TAG,"LocationChanged");
    }

    public void onProviderDisabled(String provider) {
		Log.i(TAG,"ProviderDisabled");
    }

    public void onProviderEnabled(String provider) {
		Log.i(TAG,"ProviderEnabled");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i(TAG,"tatusChanged");
    }

    private void cleanLocation() {
        locationManager.removeUpdates(this);
    }
}
