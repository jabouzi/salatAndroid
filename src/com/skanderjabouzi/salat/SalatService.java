package com.skanderjabouzi.salat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Vibrator;
import android.content.Intent;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;

public class SalatService extends IntentService {
    
    private static final String TAG = "SalatService";

    private NotificationManager notificationManager; 
    private Notification notification;

    public SalatService() {
        super(TAG);

        Log.d(TAG, "SalatService constructed");
    }

    @Override
    protected void onHandleIntent(Intent inIntent) { 
        Intent intent;
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
        this.notification.defaults |= Notification.DEFAULT_VIBRATE;
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
        Intent i = new Intent("android.intent.action.MAIN").putExtra("salatTime", salatName);
        this.sendBroadcast(i);        
    }

    private void sendTimelineNotification(String salatName) {        
        Log.d(TAG, "sendTimelineNotification'ing");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, new Intent(this, SalatActivity.class), PendingIntent.FLAG_UPDATE_CURRENT); 
        this.notification.when = System.currentTimeMillis();        
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
        Log.d(TAG, "sendTimelineNotificationed");
    }
}
