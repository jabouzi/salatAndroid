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

    public static final String MIDNIGHT_INTENT = "com.skanderjabouzi.salat.MIDNIGHT_INTENT";
    public static final String NEW_STATUS_EXTRA_COUNT = "NEW_STATUS_EXTRA_COUNT";
    public static final String RECEIVE_SALATTIME_NOTIFICATIONS = "com.skanderjabouzi.salat.RECEIVE_SALATTIME_NOTIFICATIONS";

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
        Log.d(TAG, "onHandleIntent #1 " +  mHour + " - " + mMinute+ " - " + mSeconds + " -> " + salatApp.FIRST_TIME);
        if (salatApp.FIRST_TIME == true)
        {
            Log.d(TAG, "onHandleIntent #2 " + salatApp.FIRST_TIME);
            salatApp.FIRST_TIME = false;
            salatApp.startAlarm(getApplicationContext());
        }
        else
        {    
            this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
            this.notification = new Notification(R.drawable.makka,"", 0);         

            Log.d(TAG, "onHandleIntent #3 " + mHour + "  " + mMinute+ "  " + mSeconds);
            String currentSalat = salatApp.getCurrentSalat();
            sendTimelineNotification(currentSalat);
            salatApp.startAlarm(getApplicationContext());
            if ("Midnight" == currentSalat) {
                Log.d(TAG, "It's midnight");
                intent = new Intent(MIDNIGHT_INTENT); 
                intent.putExtra(NEW_STATUS_EXTRA_COUNT, salatApp.getCurrentSalat());     
                sendBroadcast(intent, RECEIVE_SALATTIME_NOTIFICATIONS);
            }
        }
    }

    /**
    * Creates a notification in the notification bar telling user there are new
    * messages
    * 
    * @param timelineUpdateCount
    *          Number of new statuses
    */
    private void sendTimelineNotification(String currentSalat) {
        Log.d(TAG, "sendTimelineNotification'ing");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, new Intent(this, SalatActivity.class), PendingIntent.FLAG_UPDATE_CURRENT); 
        this.notification.when = System.currentTimeMillis(); 
        this.notification.flags |= Notification.FLAG_AUTO_CANCEL;
        CharSequence notificationTitle = this.getText(R.string.msgNotificationTitle); 
        CharSequence notificationSummary = this.getString(R.string.msgNotificationMessage, currentSalat);
        this.notification.setLatestEventInfo(this, notificationTitle, notificationSummary, pendingIntent); 
        this.notificationManager.notify(0, this.notification);
        Log.d(TAG, "sendTimelineNotificationed");
    }
}
