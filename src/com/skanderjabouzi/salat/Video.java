package com.skanderjabouzi.salat;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.VideoView;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

public class Video extends Activity {
  
	String fileName;
  
   @Override
   public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//final KeyguardManager.KeyguardLock kl=km.newKeyguardLock("SALAT");
		//kl.disableKeyguard();
		Log.d("VIDEO", "START");
		Intent intent = getIntent();
		String type = intent.getStringExtra("TYPE");
		if (type.equals("FAJR"))
		{
			fileName = "android.resource://com.skanderjabouzi.salat/raw/fajr";
		}
		else
		{
			fileName = "android.resource://com.skanderjabouzi.salat/raw/athan";
		}
		setContentView(R.layout-land.video);
		VideoView myVideoView = (VideoView)findViewById(R.id.myvideoview);
		myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mediaPlayer) {
				//AthanService.this.kl.reenableKeyguard();
				WakeLock.release();
				WakeLock.lock();
			}
		});
		myVideoView.setVideoURI(Uri.parse(fileName));
		myVideoView.setMediaController(new MediaController(this));
		myVideoView.requestFocus();
		myVideoView.start();
   }
   
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onStop() {
        super.onPause();
        //AthanService.this.kl.reenableKeyguard();
        WakeLock.release();
        WakeLock.lock();
    }
    
	@Override
    protected void onDestroy() {
        super.onPause();
        //AthanService.this.kl.reenableKeyguard();
        WakeLock.release();
        WakeLock.lock();
    }
}
