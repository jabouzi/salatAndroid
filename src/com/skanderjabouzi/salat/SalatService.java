package com.skanderjabouzi.salat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Vibrator;
import android.os.PowerManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;
import android.media.MediaPlayer;

public class SalatService extends IntentService {
    
    private static final String TAG = "SalatService";
    
    public static final String MIDNIGHT_INTENT = "com.skanderjabouzi.salat.MIDNIGHT_INTENT";
    public static final String SALATTIME = "SALATTIME";
    public static final String RECEIVE_SALATTIME_NOTIFICATIONS = "com.skanderjabouzi.salat.RECEIVE_SALATTIME_NOTIFICATIONS";

    private NotificationManager notificationManager; 
    private Notification notification;
    private PowerManager pm;
    private PowerManager.WakeLock wl;
    private MediaPlayer mMediaPlayer;

    public SalatService() {
        super(TAG);        
        Log.d(TAG, "SalatService constructed");
    }

    @Override
    protected void onHandleIntent(Intent inIntent) { 
        Intent intent;
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        wl.acquire();
        //this.notification.defaults |= Notification.DEFAULT_VIBRATE;
        //this.notification.defaults |= Notification.DEFAULT_LIGHTS;
        SalatApplication salatApp = (SalatApplication) getApplication();  
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        Date date = cal.getTime();
        int mHour = date.getHours();
        int mMinute = date.getMinutes();
        int mSeconds = date.getSeconds();       
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
        this.notification = new Notification(R.drawable.makka_icon,"", 0);         
        //this.notification.defaults |= Notification.DEFAULT_VIBRATE;
        Log.d(TAG, "onHandleIntent #3 " + mHour + "  " + mMinute+ "  " + mSeconds);
        String salatName = salatApp.getNextSalat();
        /*long[] vibrate = {0,100,200,300};
        this.notification.vibrate = vibrate;
        this.notification.ledARGB = 0xff00ff00;
        this.notification.ledOnMS = 300;
        this.notification.ledOffMS = 1000;
        this.notification.flags |= Notification.FLAG_SHOW_LIGHTS;*/
        sendTimelineNotification(salatName);
        salatApp.startAlarm(getApplicationContext());
        //Intent i = new Intent("android.intent.action.MAIN").putExtra("salatTime", salatName);
        //this.sendBroadcast(i);   
        if ("Midnight" == salatName) {
            //Log.d(TAG, "It's midnight");
            intent = new Intent(MIDNIGHT_INTENT); 
            intent.putExtra(SALATTIME, salatName);     
            sendBroadcast(intent, RECEIVE_SALATTIME_NOTIFICATIONS);
        }           
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
        startService(new Intent(this, AthanService.class));
        wl.release();
        Log.d(TAG, "sendTimelineNotificationed");
    }
    
    
}
