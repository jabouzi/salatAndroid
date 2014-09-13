package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.util.Log;
import android.widget.Toast;

public class SalatTimeReciever extends BroadcastReceiver {
		
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		
		Log.i("ACTION2", action);
		if (action.equals("android.intent.action.TIME_SET") || action.equals("android.intent.action.TIMEZONE_CHANGED"))
		{
			SalatApplication salatApp = new SalatApplication(context);
			long timeToSalat = salatApp.getTimeToSalat();
			Intent athanIntent = new Intent(context, SalatReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, athanIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);
			Log.i("SalatTimeReciever", "Next salat is " + salatApp.nextSalat  + " in " + timeToSalat);
		}

		Log.d("SalatTimeReceiver", "DATE CHANGED");
	}
}
