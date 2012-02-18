package com.skanderjabouzi.salat;

import java.io.IOException;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
//import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

public class SalatService extends IntentService {	
	static final String TAG = "SalatService";

	  public SalatService() {
	    super(TAG);
	  }

	  @Override
	  public void onCreate() {
	    super.onCreate();
	    Log.d(TAG, "onCreate");
	  }

	  @Override
	  protected void onHandleIntent(Intent intent) {
	    Log.d(TAG, "onHandleIntent for action: " + intent.getAction());
	  }

	  @Override
	  public void onDestroy() {
	    super.onDestroy();
	    Log.d(TAG, "onDestroy");
	  }

}
