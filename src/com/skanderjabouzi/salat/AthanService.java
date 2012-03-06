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
    public int salat;
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        salat = SalatApplication.nextSalat;
        SalatApplication salatApp = (SalatApplication) getApplication(); 
        if (0 == SalatApplication.nextSalat)
        {
			player = MediaPlayer.create(this, R.raw.fajr);
			//player = MediaPlayer.create(this, R.raw.bismillah);
		}
		else if (5 > SalatApplication.nextSalat)
        {
			player = MediaPlayer.create(this, R.raw.athan);
			//player = MediaPlayer.create(this, R.raw.bismillah);
		}        
        
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                stopService();
                //WakeLock.release();
                //AthanService.isPlaying = false;
                Log.d("AthanService","stop1");
            }
        });
        
        player.start();
        player.setLooping(false);
        //AthanService.isPlaying = true;
        Log.d("AthanService", "start " + salat);
    }    

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
        WakeLock.release();
        //AthanService.isPlaying = false;
        Log.d("AthanService","stop2");
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
