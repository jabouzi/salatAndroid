package com.skanderjabouzi.salat;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.net.Uri;
import android.widget.VideoView;
 
public class Video extends Activity {
  
	String fileName;
  
   @Override
   public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		setContentView(R.layout.video);
		VideoView myVideoView = (VideoView)findViewById(R.id.myvideoview);
		myVideoView.setVideoURI(Uri.parse(fileName));
		myVideoView.setMediaController(new MediaController(this));
		myVideoView.requestFocus();
		myVideoView.start();
   }
}
