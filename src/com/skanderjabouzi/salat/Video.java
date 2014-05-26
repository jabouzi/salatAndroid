package com.skanderjabouzi.salat;
 
import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.net.Uri;
import android.widget.VideoView;
 
public class Video extends Activity {
  
 String fileName = "android.resource://com.skanderjabouzi.salat/raw/athan";
  
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.video);
       VideoView myVideoView = (VideoView)findViewById(R.id.myvideoview);
       myVideoView.setVideoURI(Uri.parse(fileName));
       myVideoView.setMediaController(new MediaController(this));
       myVideoView.requestFocus();
       myVideoView.start();
   }
}
