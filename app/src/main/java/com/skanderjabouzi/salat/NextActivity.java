package com.skanderjabouzi.salat;

import android.view.View;
import android.view.MotionEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.Display;
import android.util.Log;
import android.widget.Toast;

public class NextActivity extends Activity {

    static final String SEND_SALATTIME_NOTIFICATIONS = "com.skanderjabouzi.salat.SEND_SALATTIME_NOTIFICATIONS";
    String sataTimes[] = new String[7];
    String[] hijriDates = new String[4];
    SalatApplication salatApp;
    MidnightReceiver receiver;
    IntentFilter filter;
    View salatView;
    OnSwipeTouchListener onSwipeTouchListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        salatView = findViewById(R.id.salatView);
        
        onSwipeTouchListener = new OnSwipeTouchListener(this) {
			public void onSwipeTop() {
				//Toast.makeText(NextActivity.this, "top", Toast.LENGTH_SHORT).show();
			}
			public void onSwipeRight() {
				//Toast.makeText(NextActivity.this, "right", Toast.LENGTH_SHORT).show();
				NextActivity.this.startActivity(new Intent(NextActivity.this, SalatActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				NextActivity.this.finish();
				NextActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
			public void onSwipeLeft() {
				//Toast.makeText(NextActivity.this, "left", Toast.LENGTH_SHORT).show();
				NextActivity.this.startActivity(new Intent(NextActivity.this, SalatActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				NextActivity.this.finish();
				NextActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			public void onSwipeBottom() {
				//Toast.makeText(NextActivity.this, "bottom", Toast.LENGTH_SHORT).show();
			}
		};	
		salatView.setOnTouchListener(onSwipeTouchListener);
        salatApp = SalatApplication.getInstance(this);
        receiver = new MidnightReceiver();
        filter = new IntentFilter( MidnightService.MIDNIGHT_INTENT );
        Log.i("NextActivity", "Created");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSalatTimes();
        super.registerReceiver(receiver, filter, SEND_SALATTIME_NOTIFICATIONS, null);
        Log.i("NextActivity", "Reseumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
        if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
    }
    
     @Override
    protected void onStop() {
        super.onStop();
        //finish();
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
    }
    
	@Override
    protected void onDestroy() {
        super.onDestroy();
        //finish();
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        onSwipeTouchListener.getGestureDetector().onTouchEvent(ev); 
            return super.dispatchTouchEvent(ev);   
    }

    private void setSalatTimes()
    {
        salatApp.initCalendar();
        salatApp.setSalatTimes(1);
        sataTimes = salatApp.getSalatTimes();
        printFajrTime();
        printDuhrTime();
        printAsrTime();
        printMaghribTime();
        printIshaTime();
        printShouroukTime();
        printHijriDate();
        printLocation();
        Log.i("NextActivity", "setSalatTimes");
    }
    
    public void printFajrTime()
    {
        TextView fajrText =    (TextView)  findViewById(R.id.fajrText);
        fajrText.setText(sataTimes[0]);
    }

    public void printDuhrTime()
    {
        TextView duhrText =    (TextView)  findViewById(R.id.duhrText);
        duhrText.setText(sataTimes[2]);
    }

    public void printAsrTime()
    {
        TextView asrText =    (TextView)  findViewById(R.id.asrText);
        asrText.setText(sataTimes[3]);
    }

    public void printMaghribTime()
    {
        TextView maghribText =    (TextView)  findViewById(R.id.maghribText);
        maghribText.setText(sataTimes[5]);
    }

    public void printIshaTime()
    {
        TextView ishaText =    (TextView)  findViewById(R.id.ishaText);
        ishaText.setText(sataTimes[6]);
    }

    public void printShouroukTime()
    {
        TextView shouroukText =    (TextView)  findViewById(R.id.shouroukText);
        shouroukText.setText(sataTimes[1]);
    }

    public void printHijriDate()
    {
        TextView date1Text =    (TextView)  findViewById(R.id.date1Text);
        date1Text.setText(this.getString(R.string.titleNext));
    }

    public void printLocation()
    {
        TextView locationText =    (TextView)  findViewById(R.id.locationText);
        locationText.setText(salatApp.getCity());
    }


    class MidnightReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            setSalatTimes();
            String salatName = intent.getStringExtra("SALATTIME");
            Toast.makeText(context, "It's Salat " + salatName + "time ", Toast.LENGTH_LONG).show();
            Log.i("SalatReceiver", salatName);
        }
    }
}
