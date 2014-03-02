package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SalatReceiver extends BroadcastReceiver {     

  @Override
  public void onReceive(Context context, Intent intent) { 
    //~ WakeLock.acquire(context);    
    //SalatApplication salatApp = (SalatApplication) getApplication(); 
    //this.startAthan(context);
    context.startService(new Intent(context, AthanService.class)); 
    Log.i("SalatReceiver", "onReceived");
  }
}
