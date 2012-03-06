package com.skanderjabouzi.salat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Arrays;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;

public class SalatApplication extends Application implements OnSharedPreferenceChangeListener {
    
    private static final String TAG = SalatApplication.class.getSimpleName();
    public static final int FAJR = 0;
    public static final int DUHR = 1;
    public static final int ASR = 2;
    public static final int MAGHRIB = 3;
    public static final int ISHA = 4;
    public static final int MIDNIGHT = 5;
    
    private SharedPreferences prefs;
    //private boolean serviceRunning;
    private String salaTimes[] = new String[7];
    private String[] hijriDates = new String[4];    
    private int year;
    private int month;
    private int day;
    //private boolean isSalat;    
    protected Toast mToast; 
    public static int nextSalat;
    public String[] salatNames = new String[5];
    
    
    @Override
      public void onCreate() {
        super.onCreate();
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefs.registerOnSharedPreferenceChangeListener(this);
        salatNames[0] = "Fajr";
        salatNames[1] = "Duhr";
        salatNames[2] = "Asr";
        salatNames[3] = "Maghrib";
        salatNames[4] = "Isha";        
        Log.i(TAG, "onCreated");
      }

      @Override
      public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "onTerminated");
      }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        // TODO Auto-generated method stub
        
    }
    
    public void initCalendar()
    {        
        Calendar cal = Calendar.getInstance();
        year = cal.get(java.util.Calendar.YEAR);
        month = cal.get(java.util.Calendar.MONTH);
        day = cal.get(java.util.Calendar.DAY_OF_MONTH); 
    }
    
    public void setSalatTimes()
    {        
        Salat prayers = new Salat();
        prayers.setCalcMethod(2);
        prayers.setAsrMethod(0);
        prayers.setDhuhrMinutes(0);
        prayers.setHighLatsMethod(0);
        
        salaTimes = prayers.getDatePrayerTimes(year,month+1,day,45.5454,-73.6391,-5);
        
/*
        salaTimes[0] = "22:25";
        salaTimes[2] = "22:26";
        salaTimes[3] = "22:27";
        salaTimes[5] = "22:28";
        salaTimes[6] = "22:29";
*/


        Log.i("app", "Sataltimes : "+java.util.Arrays.asList(salaTimes).toString());
    }
    
    public String[] getSalatTimes()
    {
        return salaTimes;
    }    
    
    public void setHijriDate()
    {
        Hijri hijri = new Hijri();        
        hijriDates = hijri.isToString(year,month+1,day,0);        
    }
    
    public String[] getHijriDates()
    {
        return hijriDates;
    }
    
    public long getTimeLeft()
    {
        long timeLeft = 0;
        if (getFajr() > 0) 
        {
            nextSalat = FAJR;
            isSalat = true;
            timeLeft = getFajr();
        }
        else if (getDuhr() > 0) 
        {
            nextSalat = DUHR;
            isSalat = true;
            timeLeft = getDuhr();
        }
        else if (getAsr() > 0) 
        {
            nextSalat = ASR;
            isSalat = true;
            timeLeft = getAsr();
        }
        else if (getMaghrib() > 0)
        {
            nextSalat = MAGHRIB;
            isSalat = true;
            timeLeft = getMaghrib();
        }
        else if (getIsha() > 0)
        {
            nextSalat = ISHA;
            isSalat = true;
            timeLeft = getIsha();
        }
        else if (getMidNight() > 0)
        {  
            nextSalat = MIDNIGHT;
            isSalat = false;
            timeLeft = getMidNight();
        }        
        return timeLeft;
    }
    
/*
    public String getNextSalat()
    {
        return SalatApplication.nextSalat;
    }
*/

/*
    public boolean isSalat()
    {
        return isSalat;
    }
*/
    
    public void startAlarm(Context context)
    {
        Calendar now = Calendar.getInstance();
        this.initCalendar();
        this.setSalatTimes();
        long timeToSalat = this.getTimeLeft() + now.getTimeInMillis();     

        Intent intent = new Intent(context, SalatService.class);  
        PendingIntent pendingIntent = PendingIntent.getService(context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT); 

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);    
        Log.i("app", "Next salat is " + nextSalat  + " in " + timeToSalat);

    }
  
    public void stopAlarm(Context context)
    {
        Intent intent = new Intent(context, SalatService.class); 
        PendingIntent pendingIntent = PendingIntent.getService(context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT); 
        
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.i("app", "Alarm Stopped");
    }
    
    private long getFajr()
    {
        String[] times = salaTimes[0].split(":");
        Calendar time = Calendar.getInstance();
        time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
        long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
        Log.i("app", "fajr : "+diff);      
        return diff;
    }
    
    private long getDuhr()
    {
        String[] times = salaTimes[2].split(":");
        Calendar time = Calendar.getInstance();
        time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
        long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
        Log.i("app", "duhr : "+diff);   
        return diff;
    }
    
    private long getAsr()
    {
        String[] times = salaTimes[3].split(":");
        Calendar time = Calendar.getInstance();
        time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
        long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
        Log.i("app", "asr : "+diff);   
        return diff;
    }
    
    private long getMaghrib()
    {
        String[] times = salaTimes[5].split(":");
        Calendar time = Calendar.getInstance();
        time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
        long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
        Log.i("app", "maghrib : "+diff);   
        return diff;
    }
    
    private long getIsha()
    {
        String[] times = salaTimes[6].split(":");
        Calendar time = Calendar.getInstance();
        time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
        long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
        Log.i("app", "isha : "+diff);    
        return diff;
    }
    
    private long getMidNight()
    {
        Calendar time = Calendar.getInstance();
        time.set(year, month, day, 24, 0);
        long diff = getTimeInMS(24,0,1) - getCurrentTimeInMS();
        Log.i("app", "midnight : "+diff);   
        return diff;
    }
    
    private long getCurrentTimeInMS()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("mm");
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH");              
         
        long ms1 = Long.parseLong(sdf1.format(cal.getTime()))*1000;
        long ms2 = Long.parseLong(sdf2.format(cal.getTime()))*60*1000;
        long ms3 = Long.parseLong(sdf3.format(cal.getTime()))*3600*1000;

        return ms1+ms2+ms3;    
    }
    
    private long getTimeInMS(long hour, long min, long sec)
    {
        return hour*3600*1000+min*60*1000+sec*1000;
    }    
}
