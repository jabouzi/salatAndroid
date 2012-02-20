package com.skanderjabouzi.salat;

//import java.util.Calendar;

//import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {

	context.startService(new Intent(context, SalatService.class).putExtras(intent));

    Log.d("BootReceiver", "SalatOnReceived"); 
  }
	
	

}
