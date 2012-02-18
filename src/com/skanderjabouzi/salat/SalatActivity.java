package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

public class SalatActivity extends Activity {
    
    static final String SEND_SALATTIME_NOTIFICATIONS = "com.skanderjabouzi.salat.SEND_SALATTIME_NOTIFICATIONS";
    String sataTimes[] = new String[7];
    String[] hijriDates = new String[4];    
    SalatApplication salatApp; 
    SalatReceiver receiver;
    IntentFilter filter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        salatApp = (SalatApplication) getApplication();
        //this.setSalatTimes();        
        receiver = new SalatReceiver();        
        Log.d("SalatActivity", "Created");
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        this.setSalatTimes();

        // Register the receiver
        filter = new IntentFilter( "com.skanderjabouzi.salat.MIDNIGHT_INTENT" );
        super.registerReceiver(receiver, filter, SEND_SALATTIME_NOTIFICATIONS, null);
        Log.d("SalatActivity", "registerReceiver");
    }

    @Override
    protected void onPause() {
        super.onPause();

        // UNregister the receiver
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
        Log.d("SalatActivity", "setSalatTimes");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // <4>
      getMenuInflater().inflate(R.menu.menu, menu);
      return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // <5>

      switch (item.getItemId()) {
      case R.id.calculation:
        startActivity(new Intent(this, CalculationActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
        break;      
      case R.id.audio:
        startService(new Intent(this, SalatService.class));
        break;      
     
      }
      return true;
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
    
    class SalatReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //setSalatTimes();
        //Toast.makeText(getApplicationContext(), "received", Toast.LENGTH_SHORT).show();
        String msg_for_me = intent.getStringExtra("NEW_STATUS_EXTRA_COUNT");
        Log.d("SalatActivity", msg_for_me);
    }
  }
}
