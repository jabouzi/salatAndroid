package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.util.Log;
import android.widget.Toast;

public class SalatTimeReciever extends BroadcastReceiver {

	private OptionsDataSource optionsDataSource;
	private LocationDataSource locationDataSource;
	private Options salatOptions;
	private Location salatLocation;
		
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		
		Log.i("ACTION2", action);
		if (action.equals("android.intent.action.TIME_SET") || action.equals("android.intent.action.TIMEZONE_CHANGED"))
		{
			optionsDataSource = new OptionsDataSource(context);
			optionsDataSource.open();
			salatOptions = optionsDataSource.getOptions(1);		
			locationDataSource = new LocationDataSource(context);
			locationDataSource.open();
			salatLocation = locationDataSource.getLocation(1);
			SalatApplication salatApp = new SalatApplication();
			salatApp.setOptions(salatOptions, salatLocation);
			long timeToSalat = salatApp.getTimeToSalat();
			Intent athanIntent = new Intent(context, SalatReceiver.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, athanIntent, 0);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);
			Log.i("SalatTimeReciever", "Next salat is " + salatApp.nextSalat  + " in " + timeToSalat);
		}

		Log.d("SalatTimeReceiver", "DATE CHANGED");
	}
}
