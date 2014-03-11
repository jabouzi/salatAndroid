package com.skanderjabouzi.salat;

//import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SalatReceiver extends BroadcastReceiver {     

  @Override
  public void onReceive(Context context, Intent intent) { 
    //~ WakeLock.acquire(context);    
    //SalatApplication salatApp = (SalatApplication) getApplication(); 
    //this.startAthan(context);Bundle bundle = intent.getExtras();
    //Bundle bundle = intent.getExtras();
	//String action = bundle.getString("ACTION");
	//Log.i("ACTION1", action);
    //if (action.equals("ATHAN_ALERT"))
	//{
		//final String action = intent.getAction();
		//Log.i("ACTION2", action);
		SalatApplication salatApp = (SalatApplication) context.getApplicationContext();
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
			salatApp.stopAlarm(context);
			salatApp.startAlarm(context);
			Log.i("VALIDTIME", "FALSE");
		}
	//}
  }
}
