package com.skanderjabouzi.salat;

//import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.util.Log;

public class SalatReceiver extends BroadcastReceiver {
	
	//private OptionsDataSource optionsDataSource;
	//private LocationDataSource locationDataSource;
	//private Options salatOptions;
	//private Location salatLocation;

  @Override
  public void onReceive(Context context, Intent intent) {
		
		SalatApplication salatApp = SalatApplication.getInstance(context);
		/*if (salatApp.isValidSalatTime())
		{
			if (SalatApplication.nextSalat == SalatApplication.MIDNIGHT)
			{
				context.startService(new Intent(context, MidnightService.class));
				Log.i("SalatReceiver", "onReceived 1");
			}
			else
			{
				context.startService(new Intent(context, AdhanService.class));
				Log.i("SalatReceiver", "onReceived 2");
			}
			Log.i("VALIDTIME", "TRUE");
		}
		else
		{
			Log.i("VALIDTIME", "FALSE");
		}
		//salatApp.setOptions(salatOptions, salatLocation);
		long timeToSalat = salatApp.getTimeToSalat();
		Intent adhanIntent = new Intent(context, SalatReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, adhanIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);
		Log.i("SalatReceiver", "Next salat is " + salatApp.nextSalat  + " in " + timeToSalat);
		*/
	//}
  }
}
