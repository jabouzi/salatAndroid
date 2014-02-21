package com.skanderjabouzi.salat;

import android.media.MediaPlayer;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Vibrator;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AthanService extends Service{

    private static final String TAG = "AthanService";
    
    public static final String MIDNIGHT_INTENT = "com.skanderjabouzi.salat.MIDNIGHT_INTENT";
    public static final String SALATTIME = "SALATTIME";
    public static final String RECEIVE_SALATTIME_NOTIFICATIONS = "com.skanderjabouzi.salat.RECEIVE_SALATTIME_NOTIFICATIONS";

    private NotificationManager notificationManager; 
    private Notification notification;
    private MediaPlayer player;
    private String athan;
    private int mInitialCallState;
    public int salat;
    SalatApplication salatApp;
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        this.salatApp = (SalatApplication) getApplication();
        this.startAthan();
        WakeLock.acquire(this);
        Log.d(TAG, "start " + salat);
    }    

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
        WakeLock.release();
        SalatApplication.athanPlaying = false;
        Log.d(TAG,"stop2");
    }
    
    public void play() {
        super.onCreate();
        SalatApplication.athanPlaying = true;
        salat = SalatApplication.nextSalat;
        //SalatApplication salatApp = (SalatApplication) getApplication(); 
        if (SalatApplication.FAJR == SalatApplication.nextSalat)
        {
            player = MediaPlayer.create(this, R.raw.fajr_athan);
            //player = MediaPlayer.create(this, R.raw.bismillah);
        }
        else if (SalatApplication.MIDNIGHT > SalatApplication.nextSalat)
        {
            player = MediaPlayer.create(this, R.raw.reg_athan);
            //player = MediaPlayer.create(this, R.raw.bismillah);
        }        
        
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                stopService();
                //WakeLock.release();
                //AthanService.isPlaying = false;
                Log.d(TAG,"stop1");
            }
        });
        
        player.start();
        player.setLooping(false);
        //AthanService.isPlaying = true;
        Log.d(TAG, "start " + salat);
    }    
    
    private void startAthan() { 
        Intent intent;
        
        if (SalatApplication.nextSalat < SalatApplication.MIDNIGHT) 
        {
            this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
            this.notification = new Notification(R.drawable.makka_icon,"", 0);        
            String salatName = salatApp.salatNames[SalatApplication.nextSalat];       
            sendTimelineNotification(salatName);
            play();
            Log.d(TAG, "onHandleIntent #3 " + SalatApplication.nextSalat + " : " + salatName);
        }
        else {
            intent = new Intent(MIDNIGHT_INTENT); 
            intent.putExtra(SALATTIME, "Midnight");     
            sendBroadcast(intent, RECEIVE_SALATTIME_NOTIFICATIONS);
            Log.d(TAG, "onHandleIntent #4 " + "Midnight");
            stopService();
        }
        
        salatApp.startAlarm(getApplicationContext());         
    }

    private void sendTimelineNotification(String salatName) {        
        Log.d(TAG, "sendTimelineNotification'ing");        
        PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, new Intent(this, SalatActivity.class), PendingIntent.FLAG_UPDATE_CURRENT); 
        this.notification.when = System.currentTimeMillis();   
        this.notification.defaults |= Notification.DEFAULT_VIBRATE;     
        this.notification.flags |= Notification.FLAG_AUTO_CANCEL ;
        this.notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        this.notification.ledARGB = 0xff00ff00;
        this.notification.ledOnMS = 300;
        this.notification.ledOffMS = 1000;
        this.notification.vibrate = new long[]{0,100,200,300};
        CharSequence notificationTitle = this.getText(R.string.msgNotificationTitle); 
        CharSequence notificationSummary = this.getString(R.string.msgNotificationMessage, salatName);
        this.notification.setLatestEventInfo(this, notificationTitle, notificationSummary, pendingIntent); 
        this.notificationManager.notify(0, this.notification);     
        //startService(new Intent(this, AthanService.class));
        
        Log.d(TAG, "sendTimelineNotificationed -> " + salatName);
    }
    
    private void stopService()
    {        
        stopService(new Intent(this, AthanService.class));
    }
    
    private void stop()
    {
        if (player != null) {
            try {
                player.stop();
                player.release();
            } finally {
                player = null;
            }
        }
    }
}
