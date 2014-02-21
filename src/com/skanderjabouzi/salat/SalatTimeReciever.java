package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

class SalatTimeReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {	
		final String action = intent.getAction();
		if (action.equals(Intent.ACTION_TIME_CHANGED) || action.equals(Intent.ACTION_DATE_CHANGED))
		{
			SalatApplication salatApp = (SalatApplication) context.getApplicationContext();  
			salatApp.stopAlarm(context);
			salatApp.startAlarm(context);
		}
		
		Log.i("SalatBootReceiver", Intent.ACTION_TIME_CHANGED);
	}
}
