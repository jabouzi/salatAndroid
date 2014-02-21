package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

class SalatTimeReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {	
			
    SalatApplication salatApp = (SalatApplication) context.getApplicationContext();  
    salatApp.stopAlarm(context);
    salatApp.startAlarm(context);
    Log.d("SalatBootReceiver", "SalatOnReceived");
	}
}
