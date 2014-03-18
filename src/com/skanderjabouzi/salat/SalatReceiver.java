package com.skanderjabouzi.salat;

//import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.util.Log;

public class SalatReceiver extends BroadcastReceiver {     

  @Override
  public void onReceive(Context context, Intent intent) { 
		WakeLock.acquire(context); 
		SalatApplication salatApp = (SalatApplication) context.getApplicationContext();
		long timeToSalat = salatApp.getTimeToSalat();
		Intent athanIntent = new Intent(context, SalatReceiver.class);  
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, athanIntent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);    
		Log.i("SalatReceiver", "Next salat is " + salatApp.nextSalat  + " in " + timeToSalat);
		if (salatApp.isValidSalatTime())
		{
			if (SalatApplication.nextSalat == SalatApplication.MIDNIGHT)
			{
				context.startService(new Intent(context, MidnightService.class)); 
				Log.i("SalatReceiver", "onReceived 1");
			}
			else
			{
				context.startService(new Intent(context, AthanService.class)); 
				Log.i("SalatReceiver", "onReceived 2");
			}
			Log.i("VALIDTIME", "TRUE");
		}
		else
		{
			Log.i("VALIDTIME", "FALSE");
		}
	//}
  }
}
