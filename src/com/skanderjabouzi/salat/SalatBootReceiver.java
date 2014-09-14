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
			setAlarm(context);
		}
  }
  
  public static void setAlarm(Context context) {
		SalatApplication salatApp = new SalatApplication(context);
		long timeToSalat = salatApp.getTimeToSalat();
		Intent athanIntent = new Intent(context, AthanService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, athanIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);
		Log.i("SalatBootReceiver", "Next salat is " + salatApp.nextSalat  + " in " + timeToSalat);
		Log.i("SalatBootReceiver", "SalatOnReceived");
	}
}
