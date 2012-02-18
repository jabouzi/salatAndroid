package com.skanderjabouzi.salat;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent callingIntent) {

	SalatApplication salatApp; 
    salatApp = (SalatApplication) context.getApplicationContext();
    Calendar cal = Calendar.getInstance();
    salatApp.initCalendar();
    salatApp.setSalatTimes();
    long timeToSalat = 120000; //salatApp.getTimeLeft() + cal.getTimeInMillis();     

    Intent intent = new Intent(context, SalatService.class); 
    PendingIntent pendingIntent = PendingIntent.getService(context, -1, intent,
        PendingIntent.FLAG_UPDATE_CURRENT); 

    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); 
    alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis(), timeToSalat, pendingIntent); 

    Log.d("SalatBootReceiver", "SalatOnReceived");
  }
	
	

}
