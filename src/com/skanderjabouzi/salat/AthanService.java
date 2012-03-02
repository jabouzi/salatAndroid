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
    public static boolean isPlaying = false;
    
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
            public void onCompletion(MediaPlayer player) {
                stop();
                WakeLock.release();
                AthanService.isPlaying = false;
                Log.d("AthanService","stop1");
            }
        });
        
        player.start();
        player.setLooping(false);
        AthanService.isPlaying = true;
        Log.d("AthanService", "start");
    }    

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
        WakeLock.release();
        AthanService.isPlaying = false;
        Log.d("AthanService","stop2");
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
        stopService(new Intent(this, AthanService.class));
    }
}
