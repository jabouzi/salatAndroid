package com.skanderjabouzi.salat;

import java.util.Calendar;

//import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SalatBootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {    
		final String action = intent.getAction();
		Log.i("ACTION2", action);
		if (action.equals("android.intent.action.BOOT_COMPLETED"))
		{		
			SalatApplication salatApp = (SalatApplication) context.getApplicationContext();  
			salatApp.startAlarm(context);
			Log.i("SalatBootReceiver", "SalatOnReceived");
		}
  }
}
