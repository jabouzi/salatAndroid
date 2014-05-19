package com.skanderjabouzi.salat;

import android.view.MotionEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.Display;
import android.util.Log;
import android.widget.Toast;

public class SalatActivity extends Activity {

    static final String SEND_SALATTIME_NOTIFICATIONS = "com.skanderjabouzi.salat.SEND_SALATTIME_NOTIFICATIONS";
    String sataTimes[] = new String[7];
    String[] hijriDates = new String[4];
    SalatApplication salatApp;
    MidnightReceiver receiver;
    IntentFilter filter;
	private SimpleGestureFilter detector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        salatApp = new SalatApplication(this);
        receiver = new MidnightReceiver();
        filter = new IntentFilter( MidnightService.MIDNIGHT_INTENT );
        detector = new SimpleGestureFilter(this,this);
        Log.i("SalatActivity", "Created");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //optionsDataSource.open();
        //salatOptions = optionsDataSource.getOptions(1);
        //locationDataSource.open();
		//salatLocation = locationDataSource.getLocation(1);
		//salatApp.setOptions(salatOptions, salatLocation);
        if (salatApp.checkOptions())
        {
            setSalatTimes();
        }
        else
        {
            if (salatApp.prefType == 0)
            {
                startActivity(new Intent(this, OptionsActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            }
            else if(salatApp.prefType == 1)
            {
                startActivity(new Intent(this, LocationActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            }
        }

        super.registerReceiver(receiver, filter, SEND_SALATTIME_NOTIFICATIONS, null);
        Log.i("SalatActivity", "Reseumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void setSalatTimes()
    {
        salatApp.initCalendar();
        salatApp.setSalatTimes();
        salatApp.setHijriDate();
        sataTimes = salatApp.getSalatTimes();
        hijriDates = salatApp.getHijriDates();
        printFajrTime();
        printDuhrTime();
        printAsrTime();
        printMaghribTime();
        printIshaTime();
        printShouroukTime();
        printHijriDate();
        printLocation();
        Log.i("SalatActivity", "setSalatTimes");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu, menu);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

      switch (item.getItemId()) {
      case R.id.options:
        startActivity(new Intent(this, OptionsActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        break;
      case R.id.location:
        startActivity(new Intent(this, LocationActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        break;
      case R.id.qibla:
        startActivity(new Intent(this, SalatQibla.class)
            .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        break;
      case R.id.athan:
        stopService(new Intent(this, AthanService.class));
        break;
      case R.id.play:
        startService(new Intent(this, AthanService.class));
        break;
      }
      return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        menu.findItem(R.id.athan).setVisible(SalatApplication.athanPlaying);
        return true;
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
    	// Call onTouchEvent of SimpleGestureFilter class
         this.detector.onTouchEvent(me);
       return super.dispatchTouchEvent(me);
    }
    
	@Override
	public void onSwipe(int direction) {
		String str = "";
		switch (direction) {
			case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right"; break;
			case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left"; break;
			case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down"; break;
			case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up"; break;	 
		}
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	 
	@Override
	public void onDoubleTap() {
	    Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
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
        date1Text.setText(hijriDates[0] + " " + hijriDates[1] + " " + hijriDates[3]);
    }

    public void printLocation()
    {
        TextView locationText =    (TextView)  findViewById(R.id.locationText);
        locationText.setText(salatApp.getCity() + ", " + salatApp.getCountry());
    }


    class MidnightReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
			//optionsDataSource.open();
			//salatOptions = optionsDataSource.getOptions(1);
			//locationDataSource.open();
			//salatLocation = locationDataSource.getLocation(1);
			//salatApp.setOptions(salatOptions, salatLocation);
            setSalatTimes();
            String salatName = intent.getStringExtra("SALATTIME");
            Toast.makeText(context, "It's Salat " + salatName + "time ", Toast.LENGTH_LONG).show();
            //String msg_for_me = intent.getStringExtra("NEW_STATUS_EXTRA_COUNT");
            Log.i("SalatReceiver", salatName);
        }
    }
}
