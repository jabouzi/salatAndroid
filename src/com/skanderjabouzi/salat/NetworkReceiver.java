package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver {
  public static final String TAG = "NetworkReceiver";

  @Override
	public void onReceive(Context context, Intent intent) 
	{
		final String action = intent.getAction();
		if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")  || action.equals("android.intent.action.TIMEZONE_CHANGED"))
		{
			
			SalatApplication salatApp = SalatApplication.getInstance(context);
			//salatApp.setAlarm(context);
			
			boolean isNetworkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);    
			if (isNetworkDown) 
			{
				Log.d(TAG, "onReceive: NOT connected, do nothing");
			} 
			else 
			{
				//Log.d(TAG, "onReceive: connected, check hijri date");
				//Log.i(TAG, "onReceive : connected : " + SalatApplication.isConnected);
				Log.d(TAG, "onReceive : connected, check location");
			}
		}
	}
}
