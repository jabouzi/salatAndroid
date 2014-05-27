package com.skanderjabouzi.salat;

//import android.media.MediaPlayer;
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
import android.view.WindowManager;
//import android.app.KeyguardManager;

public class AthanService extends Service{

    private static final String TAG = "ATHANSERVICE";

    private NotificationManager notificationManager;
    private Notification notification;
    //private MediaPlayer player;
    private String athan;
    private int mInitialCallState;
    public int salat = 0;
    SalatApplication salatApp;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();        
        WakeLock.acquire(this);
        WakeLock.unlock(this);
        salatApp = new SalatApplication(this);
        startAthan();
        Log.i(TAG, "start");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stop();
        SalatApplication.athanPlaying = false;
        WakeLock.release();
        Log.i(TAG,"stop2");
    }

    public void playAthan() {
        super.onCreate();
        SalatApplication.athanPlaying = true;
        //SalatApplication salatApp = (SalatApplication) getApplication();
        if (SalatApplication.FAJR == salat)
        {
			Log.i(TAG, "play -> " + salat);
			Intent intent = new Intent();
			intent.setClass(this, Video.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//intent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON + WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
			intent.putExtra("TYPE", "FAJR");
			startActivity(intent);
            //player = MediaPlayer.create(this, R.raw.fajr_athan);
            //play();
            //player = MediaPlayer.create(this, R.raw.bismillah);
        }
        else if (SalatApplication.MIDNIGHT > salat)
        {
			Log.i(TAG, "play -> " + salat);
			Intent intent = new Intent();
			intent.setClass(this, Video.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//intent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON + WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
			intent.putExtra("TYPE", "SALAT");
			startActivity(intent);
            //player = MediaPlayer.create(this, R.raw.reg_athan);
            //play();
            //player = MediaPlayer.create(this, R.raw.bismillah);
        }
        stopService();
    }

    private void startAthan() {
        Intent intent;
        salatApp.getTimeLeft();
		if (SalatApplication.nextSalat == 0) salat = 7;
        else if (SalatApplication.nextSalat == 2) salat = 0;
        else if (SalatApplication.nextSalat == 5) salat = 3;
        else salat = SalatApplication.nextSalat - 1;
        Log.i(TAG, "SALAT -> " + salat + " NEXT -> " + SalatApplication.nextSalat);
		this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		this.notification = new Notification(R.drawable.makka_icon,"", 0);
		String salatName = "";
		if (salat < 7)	salatName = salatApp.salatNames[salat];
		sendTimelineNotification(salatName);
		playAthan();
		Log.i(TAG, "onHandleIntent #3 " + salat + " : " + salatName);
    }

    private void sendTimelineNotification(String salatName) {

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

        Log.i(TAG, "sendTimelineNotificatione -> " + salatName);
    }

    private void play()
    {
		/*player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                stopService();
                //WakeLock.release();
                //AthanService.isPlaying = false;
                Log.i(TAG,"stop1");
            }
        });

        //player.start();
        //player.setLooping(false);
        //AthanService.isPlaying = true;
        Log.i(TAG, "start " + salat);*/
	}

    private void stopService()
    {
        stopService(new Intent(this, AthanService.class));
    }

    private void stop()
    {
        //if (player != null) {
            //try {
                //player.stop();
                //player.release();
            //} finally {
                //player = null;
            //}
        //}
    }
}
