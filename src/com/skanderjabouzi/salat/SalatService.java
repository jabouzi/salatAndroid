package com.skanderjabouzi.salat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
        SalatApplication salatApp = (SalatApplication) getApplication();  
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        Date date = cal.getTime();
        int mHour = date.getHours();
        int mMinute = date.getMinutes();
        int mSeconds = date.getSeconds();       
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
        this.notification = new Notification(R.drawable.makka_icon,"", 0);         

        Log.d(TAG, "onHandleIntent #3 " + mHour + "  " + mMinute+ "  " + mSeconds);
        String salatName = salatApp.getNextSalat();
        sendTimelineNotification(salatName);
        salatApp.startAlarm(getApplicationContext());
        Intent i = new Intent("android.intent.action.MAIN").putExtra("salatTime", salatName);
        this.sendBroadcast(i);     
    }

    private void sendTimelineNotification(String salatName) {
        Log.d(TAG, "sendTimelineNotification'ing");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, new Intent(this, SalatActivity.class), PendingIntent.FLAG_UPDATE_CURRENT); 
        this.notification.when = System.currentTimeMillis(); 
        this.notification.flags |= Notification.FLAG_AUTO_CANCEL;
        CharSequence notificationTitle = this.getText(R.string.msgNotificationTitle); 
        CharSequence notificationSummary = this.getString(R.string.msgNotificationMessage, salatName);
        this.notification.setLatestEventInfo(this, notificationTitle, notificationSummary, pendingIntent); 
        this.notificationManager.notify(0, this.notification);
        Log.d(TAG, "sendTimelineNotificationed");
    }
}
