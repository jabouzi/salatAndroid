package com.skanderjabouzi.salat;

import android.media.MediaPlayer;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AthanService extends Service{

    private MediaPlayer player;
    private String athan;
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        SalatApplication salatApp = (SalatApplication) getApplication(); 
        String salatName = salatApp.getNextSalat();
        if ("Fajr" == salatName)
        {
			player = MediaPlayer.create(this, R.raw.fajr);
		}
		else if ("Midnight" != salatName)
        {
			player = MediaPlayer.create(this, R.raw.athan);
		}        
        
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.stop();
                player.release();
                WakeLock.release();
            }
        });
        player.start();
        player.setLooping(false);
        Log.d("AthanService", "start");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        WakeLock.release();
        Log.d("AthanService","stop");
    }
}
