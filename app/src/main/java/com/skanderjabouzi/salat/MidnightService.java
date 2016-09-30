package com.skanderjabouzi.salat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MidnightService extends Service{

    private static final String TAG = "MidhightService";

    public static final String MIDNIGHT_INTENT = "com.skanderjabouzi.salat.MIDNIGHT_INTENT";
    public static final String SALATTIME = "SALATTIME";
    public static final String RECEIVE_SALATTIME_NOTIFICATIONS = "com.skanderjabouzi.salat.RECEIVE_SALATTIME_NOTIFICATIONS";

    SalatApplication salatApp;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        salatApp = SalatApplication.getInstance(this);
        changeDay();
        Log.i(TAG, "start" );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"stop2");
    }

    private void changeDay() {
        Intent intent;

        intent = new Intent(MIDNIGHT_INTENT);
        intent.putExtra(SALATTIME, "Midnight");
        sendBroadcast(intent, RECEIVE_SALATTIME_NOTIFICATIONS);
        Log.i(TAG, "onHandleIntent #4 " + "Midnight");
        stopService();
    }

    private void stopService()
    {
        stopService(new Intent(this, MidnightService.class));
    }
}
