package com.skanderjabouzi.salat;

import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.util.Log;

public class SalatBootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {    
		final String action = intent.getAction();
		Log.i("ACTION2", action);
		if (action.equals("android.intent.action.BOOT_COMPLETED"))
		{		
			SalatApplication salatApp = (SalatApplication) context.getApplicationContext();  
			long timeToSalat = salatApp.getTimeToSalat();
			Intent intent = new Intent(context, SalatReceiver.class);  
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);    
			Log.i("SalatBootReceiver", "Next salat is " + salatApp.nextSalat  + " in " + timeToSalat);
			Log.i("SalatBootReceiver", "SalatOnReceived");
		}
  }
}
