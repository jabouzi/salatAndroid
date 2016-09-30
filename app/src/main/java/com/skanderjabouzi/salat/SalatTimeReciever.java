package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.util.Log;
import android.widget.Toast;

public class SalatTimeReciever extends BroadcastReceiver {
		
	SalatApplication salatApp;
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		
		Log.i("ACTION II", action);
		if (action.equals("android.intent.action.TIME_SET"))
		{
			salatApp = SalatApplication.getInstance(context);
			salatApp.setAlarm(context, "TIME");
		}

		Log.d("SalatTimeReceiver", "DATE CHANGED");
	}
}
