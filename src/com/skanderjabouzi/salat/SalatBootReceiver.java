package com.skanderjabouzi.salat;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SalatBootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
      
    SalatApplication salatApp = (SalatApplication) context.getApplicationContext();  
    salatApp.startAlarm(context);
    Log.d("SalatBootReceiver", "SalatOnReceived");
  }
}
