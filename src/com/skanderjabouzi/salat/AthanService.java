package com.skanderjabouzi.salat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    private static final String TAG = "Salat AthanService";
    
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
        //SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		//Date date = new Date();
		//String now = dateFormat.format(date);
		//String equal = "False";
        //if (this.salatApp.getSalatTimes()[SalatApplication.nextSalat] == now) equal = "True";
        //Log.i(TAG, "ALARM TIME : " + this.salatApp.getSalatTimes()[SalatApplication.nextSalat] + " -> " + now + " - " + equal);
        //if (this.salatApp.getSalatTimes()[SalatApplication.nextSalat] == now)
        //{
			startAthan();
			WakeLock.acquire(this);
		//}
        Log.i(TAG, "start " + SalatApplication.nextSalat);
    }    

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
        WakeLock.release();
        SalatApplication.athanPlaying = false;
        Log.i(TAG,"stop2");
    }
    
    public void play() {
        super.onCreate();
        SalatApplication.athanPlaying = true;
        //salat = SalatApplication.nextSalat;
        //SalatApplication this.salatApp = (SalatApplication) getApplication(); 
        Log.i(TAG, "##MEDIAPLAYER## --> " + SalatApplication.nextSalat);
        if (SalatApplication.FAJR == SalatApplication.nextSalat)
        {
            player = MediaPlayer.create(this, R.raw.fajr_athan);
            player.start();
			player.setLooping(false);
            //player = MediaPlayer.create(this, R.raw.bismillah);
        }
        else if (SalatApplication.MIDNIGHT > SalatApplication.nextSalat)
        {
            player = MediaPlayer.create(this, R.raw.reg_athan);
            player.start();
			player.setLooping(false);
            //player = MediaPlayer.create(this, R.raw.bismillah);
        }        
        
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                stopService();
                //WakeLock.release();
                //AthanService.isPlaying = false;
                Log.i(TAG,"stop1");
            }
        });
        
        //AthanService.isPlaying = true;
        Log.i(TAG, "start " + salat);
    }    
    
    private void startAthan() { 
        Intent intent;
        Log.i(TAG, "##SERVICE## --> " + SalatApplication.nextSalat + " - " + SalatApplication.MIDNIGHT);
        if (SalatApplication.nextSalat < SalatApplication.MIDNIGHT) 
        {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			Date date = new Date();
			String now = dateFormat.format(date);
			String equal = "False";
			if (this.salatApp.getSalatTimes()[SalatApplication.nextSalat].toString().equals(now.toString())) equal = "True";
			Log.i(TAG, "ALARM TIME : " + this.salatApp.getSalatTimes()[SalatApplication.nextSalat] + " -> " + now + " - " + equal);
			if (this.salatApp.getSalatTimes()[SalatApplication.nextSalat].toString().equals(now.toString()))
			{
				this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
				this.notification = new Notification(R.drawable.makka_icon,"", 0);        
				String salatName = this.salatApp.salatNames[SalatApplication.nextSalat];       
				sendTimelineNotification(salatName);
				play();
				Log.i(TAG, "onHandleIntent #3 " + SalatApplication.nextSalat + " : " + salatName);
			}
        }
        else {
            intent = new Intent(MIDNIGHT_INTENT); 
            intent.putExtra(SALATTIME, "Midnight");     
            sendBroadcast(intent, RECEIVE_SALATTIME_NOTIFICATIONS);
            Log.i(TAG, "onHandleIntent #4 " + "Midnight");
            stopService();
        }
        
        this.salatApp.startAlarm(getApplicationContext());           
    }

    private void sendTimelineNotification(String salatName) {        
        Log.i(TAG, "sendTimelineNotification'ing");        
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
        
        Log.i(TAG, "sendTimelineNotificationed -> " + salatName);
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
