package com.skanderjabouzi.salat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class SalatActivity extends Activity {
	
	String sataTimes[] = new String[7];
	String[] hijriDates = new String[4];	
	SalatApplication salatApp; 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        salatApp = (SalatApplication) getApplication();
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
    	TextView fajrText =	(TextView)  findViewById(R.id.fajrText);
        fajrText.setText(sataTimes[0]); 
    }
    
    public void printDuhrTime()
    {
    	TextView duhrText =	(TextView)  findViewById(R.id.duhrText);
        duhrText.setText(sataTimes[2]); 
    }
    
    public void printAsrTime()
    {
    	TextView asrText =	(TextView)  findViewById(R.id.asrText);
        asrText.setText(sataTimes[3]); 
    }
    
    public void printMaghribTime()
    {
    	TextView maghribText =	(TextView)  findViewById(R.id.maghribText);
        maghribText.setText(sataTimes[5]); 
    }
    
    public void printIshaTime()
    {
    	TextView ishaText =	(TextView)  findViewById(R.id.ishaText);
        ishaText.setText(sataTimes[6]); 
    }
    
    public void printShouroukTime()
    {
    	TextView shouroukText =	(TextView)  findViewById(R.id.shouroukText);
        shouroukText.setText(sataTimes[1]); 
    }    
    
    public void printHijriDate()
    {
		TextView date1Text =	(TextView)  findViewById(R.id.date1Text);
		date1Text.setText(hijriDates[0] + " " + hijriDates[1] + " " + hijriDates[3]); 
    }
}