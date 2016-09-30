package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver {
  public static final String TAG = "NetworkReceiver";

  @Override
	public void onReceive(Context context, Intent intent) 
	{
		final String action = intent.getAction();
		
		SalatApplication salatApp = SalatApplication.getInstance(context);
		intent = new Intent(context, LocationService.class);
		intent.putExtra("SAVE", "1");
		
		if (action.equals("android.intent.action.TIMEZONE_CHANGED")) 
		{
			if (!isNetworkAvailable(context))
			{
				intent.putExtra("SOURCE", "TIMEZONE");
				Log.d(TAG, "onReceive: NOT connected");
				context.startService(intent);
			}
		} 
		else if (action.equals("android.net.conn.CONNECTIVITY_CHANGE"))
		{
			if (isNetworkAvailable(context))
			{
				intent.putExtra("SOURCE", "NETWORK");
				//Log.d(TAG, "onReceive: connected, check hijri date");
				Log.d(TAG, "onReceive : connected, check location");
				context.startService(intent);
			}
		}

	}
	
	private boolean isNetworkAvailable(Context context) 
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
