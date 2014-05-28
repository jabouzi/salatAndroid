package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver { // <1>
  public static final String TAG = "NetworkReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {

    boolean isNetworkDown = intent.getBooleanExtra(
        ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);  // <2>
    
    if (isNetworkDown) {
      Log.d(TAG, "onReceive: NOT connected, do nothing");
    } else {
      Log.d(TAG, "onReceive: connected, check hijri date");
    }
  }

}
