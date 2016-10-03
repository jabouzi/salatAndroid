package com.skanderjabouzi.salat;

import android.app.AlarmManager;
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
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class AdhanService extends Service{

    private static final String TAG = "ADHANSERVICE";
    public static final String MIDNIGHT_INTENT = "com.skanderjabouzi.salat.MIDNIGHT_INTENT";
    public static final String SALATTIME = "SALATTIME";
    public static final String RECEIVE_SALATTIME_NOTIFICATIONS = "com.skanderjabouzi.salat.RECEIVE_SALATTIME_NOTIFICATIONS";

    private NotificationManager notificationManager;
    private Notification notification;
    private String adhan;
    private int mInitialCallState;
    //SalatApplication salatApp;
    public int nextSalat = -1;
    public int athanType = -1;
    String salatName = "";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        WakeLock.acquire(this, "AdhanService");

        //salatApp = SalatApplication.getInstance(this);
        //nextSalat = SalatApplication.nextSalat;
        //Log.i(TAG, "onStartCommand : nextSalat : " + nextSalat);
        //SalatApplication.write2sd(TAG, "NEXT SATAT : " + String.valueOf(SalatApplication.nextSalat));

        long timeMS = Calendar.getInstance().getTimeInMillis();
        String timeStamp1 = new SimpleDateFormat("HH:mm").format(timeMS);
        String timeStamp2 = new SimpleDateFormat("HH:mm").format(timeMS + 60000);
        String timeStamp3 = new SimpleDateFormat("HH:mm").format(timeMS - 60000);
        String extraString = intent.getStringExtra("TIME");
        nextSalat = Integer.parseInt(intent.getStringExtra("NEXT"));
        athanType = Integer.parseInt(intent.getStringExtra("ADHAN"));
        salatName = intent.getStringExtra("NAME");
        Log.i(TAG, "onStartCommand : TIME : " + extraString);
        SalatApplication.write2sd(TAG, "onStartCommand : TIME : " + extraString);

        Log.i(TAG, "onStartCommand : timeStamp1 : " + timeStamp1);
        Log.i(TAG, "onStartCommand : timeStamp2 : " + timeStamp2);
        Log.i(TAG, "onStartCommand : timeStamp3 : " + timeStamp3);
        SalatApplication.write2sd(TAG, "timeStamp : " + extraString + " " + timeStamp1 + " " + timeStamp2 + " " + timeStamp3);

        Log.i(TAG, "onStartCommand : NEXT SATAT : " + String.valueOf(nextSalat));
        SalatApplication.write2sd(TAG, "onStartCommand : NEXT SATAT : " + String.valueOf(nextSalat));

        Log.i(TAG, "onStartCommand : ADHAN TYPE : " + String.valueOf(athanType));
        SalatApplication.write2sd(TAG, "onStartCommand : ADHAN TYPE : " + String.valueOf(athanType));

        Log.i(TAG, "onStartCommand : SATAT NAME : " + salatName);
        SalatApplication.write2sd(TAG, "onStartCommand : SATAT NAME : " + salatName);

        //if (extraString.equals(timeStamp1) || extraString.equals(timeStamp2) || extraString.equals(timeStamp3))
        //{
        if (nextSalat == SalatApplication.MIDNIGHT)
        {
            changeDay();
            Log.i(TAG, "changeDay");
            SalatApplication.write2sd(TAG, "changeDay");
        }
        else
        {
            //Log.i(TAG, "getAdhan" + );
            //SalatApplication.write2sd(TAG, "getAdhan " + String.valueOf(athanType));
            startAdhan();
            Log.i(TAG, "startAdhan");
            SalatApplication.write2sd(TAG, "startAdhan");
        }
        SalatApplication.setAlarm(this, "Adhan");
        Log.i(TAG, "start");
               /* }
                else
                {
                        stopService();
                        WakeLock.release("changeDay");
                        SalatApplication.setAlarm(this, "Adhan");
                }*/
        return super.onStartCommand(intent, flags, startId);
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
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        this.notificationManager.cancel(0);
        Log.i(TAG, "onHandleIntent #4 " + "Midnight");
        WakeLock.release("changeDay");
        stopService();
    }

    private void startAdhan() {
        Intent intent;
        Log.i(TAG, " STARTADHAN -> " + nextSalat);
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        this.notification = new Notification(R.drawable.makka_icon,"", 0);
        //salatName = salatApp.salatNames[nextSalat];
        sendTimelineNotification(salatName);
        if (athanType == 1 || athanType == 3)
        {
            playAdhan();
        }
        else
        {
            stopService();
            WakeLock.release("startAdhan");
        }

        Log.i(TAG, "onHandleIntent #3 " + nextSalat + " : " + salatName);
    }

    private void playAdhan() {
        Intent intent = new Intent();
        Log.i(TAG, "play -> " + nextSalat);
        if (SalatApplication.FAJR == nextSalat)
        {
            intent.putExtra("TYPE", "FAJR");

        }
        else
        {
            intent.putExtra("TYPE", "SALAT");
        }
        intent.setClass(this, Video.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        stopService();
    }

    private void sendTimelineNotification(String salatName) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, new Intent(this, SalatActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Context context = this;
        NotificationCompat.Builder reminderNotification = new NotificationCompat.Builder(AdhanService.this);
        reminderNotification.setContentTitle(context.getString(R.string.msgNotificationTitle));
        reminderNotification.setContentText(context.getString(R.string.msgNotificationMessage, salatName));
        reminderNotification.setSmallIcon(R.drawable.makka_icon);
        reminderNotification.setContentIntent(pendingIntent);
        this.notificationManager.notify(0,reminderNotification.build());
        this.notification.when = System.currentTimeMillis();
        this.notification.flags |= Notification.FLAG_AUTO_CANCEL ;
        this.notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        this.notification.ledARGB = 0xff00ff00;
        this.notification.ledOnMS = 300;
        this.notification.ledOffMS = 1000;
        if (athanType == 2 || athanType == 3)
        {
            this.notification.defaults |= Notification.DEFAULT_VIBRATE;
            this.notification.vibrate = new long[]{0,100,200,300};
        }

        Log.i(TAG, "sendTimelineNotificatione -> " + salatName);
        SalatApplication.write2sd(TAG, "sendTimelineNotification : SATAT NAME : " + salatName);
    }

    private void stopService()
    {
        stopService(new Intent(this, AdhanService.class));
        Log.i(TAG, "stopService");
    }
}
