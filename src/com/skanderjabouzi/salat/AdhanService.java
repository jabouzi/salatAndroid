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

public class AdhanService extends Service{

    private static final String TAG = "ADHANSERVICE";
    public static final String MIDNIGHT_INTENT = "com.skanderjabouzi.salat.MIDNIGHT_INTENT";
    public static final String SALATTIME = "SALATTIME";
    public static final String RECEIVE_SALATTIME_NOTIFICATIONS = "com.skanderjabouzi.salat.RECEIVE_SALATTIME_NOTIFICATIONS";

    private NotificationManager notificationManager;
    private Notification notification;
    private String adhan;
    private int mInitialCallState;
    SalatApplication salatApp;
    public int nextSalat = -1;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    
    @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
		String extraString = intent.getStringExtra("TIME");
		Log.i(TAG, "onStartCommand : extraString : " + extraString);
		Log.i(TAG, "onStartCommand : timeStamp : " + timeStamp);
		if (extraString.equals(timeStamp))
		{
			salatApp = SalatApplication.getInstance(this);
			nextSalat = SalatApplication.nextSalat;
			Log.i(TAG, "onStartCommand : nextSalat : " + nextSalat);
			if (nextSalat == SalatApplication.MIDNIGHT)
			{
				changeDay();
				Log.i(TAG, "changeDay");
			}
			else
			{
				Log.i(TAG, "getAdhan" + salatApp.getAdhan());
				startAdhan();
				Log.i(TAG, "startAdhan");
			}			
			salatApp.setAlarm(this, "Adhan");
			Log.i(TAG, "start");
		}
		else
		{
			stopService();
		}
		return super.onStartCommand(intent, flags, startId);
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"stop2");
    }

    private void playAdhan() {
        if (SalatApplication.FAJR == nextSalat)
        {
			Log.i(TAG, "play -> " + nextSalat);
			Intent intent = new Intent();
			intent.setClass(this, Video.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("TYPE", "FAJR");
			startActivity(intent);
        }
        else
        {
			Log.i(TAG, "play -> " + nextSalat);
			Intent intent = new Intent();
			intent.setClass(this, Video.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("TYPE", "SALAT");
			startActivity(intent);
        }
        stopService();
    }
    
    private void changeDay() {
        Intent intent;
        intent = new Intent(MIDNIGHT_INTENT);
        intent.putExtra(SALATTIME, "Midnight");
        sendBroadcast(intent, RECEIVE_SALATTIME_NOTIFICATIONS);
        Log.i(TAG, "onHandleIntent #4 " + "Midnight");
        stopService();
    }

    private void startAdhan() {
        Intent intent;
        Log.i(TAG, " STARTADHAN -> " + nextSalat);
		this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		this.notification = new Notification(R.drawable.makka_icon,"", 0);
		String salatName = "";
		salatName = salatApp.salatNames[nextSalat];
		sendTimelineNotification(salatName);
		if (salatApp.getAdhan() == 1 || salatApp.getAdhan() == 3)
		{
			playAdhan();
		}
		else
		{
			stopService();
		}

		Log.i(TAG, "onHandleIntent #3 " + nextSalat + " : " + salatName);
    }

    private void sendTimelineNotification(String salatName) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, new Intent(this, SalatActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

		this.notification.when = System.currentTimeMillis();
		this.notification.flags |= Notification.FLAG_AUTO_CANCEL ;
		this.notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		this.notification.ledARGB = 0xff00ff00;
		this.notification.ledOnMS = 300;
		this.notification.ledOffMS = 1000;
		if (salatApp.getAdhan() == 2 || salatApp.getAdhan() == 3)
		{
			this.notification.defaults |= Notification.DEFAULT_VIBRATE;
			this.notification.vibrate = new long[]{0,100,200,300};
		}
        CharSequence notificationTitle = this.getText(R.string.msgNotificationTitle);
        CharSequence notificationSummary = this.getString(R.string.msgNotificationMessage, salatName);
        this.notification.setLatestEventInfo(this, notificationTitle, notificationSummary, pendingIntent);
        this.notificationManager.notify(0, this.notification);
        
        if (salatApp.getAdhan() == 2)
        {
			stopService();
		}

        Log.i(TAG, "sendTimelineNotificatione -> " + salatName);
    }

    private void stopService()
    {
        stopService(new Intent(this, AdhanService.class));
        Log.i(TAG, "stopService");
    }
}
