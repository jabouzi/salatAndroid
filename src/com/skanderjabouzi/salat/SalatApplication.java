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

    public String[] salatNames = new String[5];
    
    private SharedPreferences salatOptions;
    //private boolean serviceRunning;
    private String salaTimes[] = new String[7];
    private String[] hijriDates = new String[4];
    
    private int year;
    private int month;
    private int day;
    private int calcMethod;
    private int asrMethod;
    private int hijriDays;
    private int highLatitude;
    private float latitude;
    private float longitude;
    private int timezone;    
    private String city;
    private String country;
    
    //private boolean isSalat;    
    protected Toast mToast; 
    public static int nextSalat;
    public static boolean athanPlaying = false;
    public static int prefType;
        
    
    @Override
      public void onCreate() {
        super.onCreate();
        salatOptions = PreferenceManager.getDefaultSharedPreferences(this);
        salatOptions.registerOnSharedPreferenceChangeListener(this);
        
        salatNames[0] = "Fajr";
        salatNames[1] = "Duhr";
        salatNames[2] = "Asr";
        salatNames[3] = "Maghrib";
        salatNames[4] = "Isha";        
        Log.i("app", "onCreated");        
      }

      @Override
      public void onTerminate() {
        super.onTerminate();
        Log.i("app", "onTerminated");
      }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        
        //Intent intent;
        this.stopAlarm(this);
        this.startAlarm(this);
        //intent = new Intent(MIDNIGHT_INTENT); 
        //intent.putExtra(SALATTIME, "Midnight");     
        //sendBroadcast(intent, RECEIVE_SALATTIME_NOTIFICATIONS);
        //Log.i("app", "sendBroadcast");
    }
    
    public void setOptions()
    {
        this.calcMethod = Integer.parseInt(salatOptions.getString("calculation", "0"));
        this.asrMethod = Integer.parseInt(salatOptions.getString("asr", "1"));
        this.hijriDays = Integer.parseInt(salatOptions.getString("hijri", "1"));
        this.highLatitude = Integer.parseInt(salatOptions.getString("highLatitudes", "1"));
        this.longitude = Float.valueOf(salatOptions.getString("longitude", "0"));
        this.latitude = Float.valueOf(salatOptions.getString("latitude", "0"));
        this.timezone = Integer.parseInt(salatOptions.getString("timezone", "0"));
        this.city = salatOptions.getString("city", " ");
        this.country = salatOptions.getString("country", " ");
        Log.i("app", "Calculation " + calcMethod + " " + asrMethod + " " + hijriDays + " " + highLatitude);
    }
    
    public boolean checkOptions()
    {
        if (Integer.parseInt(salatOptions.getString("calculation", "0")) == 0) 
        {
            this.prefType = 0;
            return false;
        }
        else if (salatOptions.getString("longitude", null) == null)
        {            
            this.prefType = 1;
            return false;
        }
        else if (salatOptions.getString("latitude", null) == null)
        {
            this.prefType = 1;
            return false;
        }
        else if (salatOptions.getString("timezone", null) == null)
        {
            this.prefType = 1;
            return false;
        }
        return true;
    }
    
    public void initCalendar()
    {        
        Calendar cal = Calendar.getInstance();
        this.year = cal.get(java.util.Calendar.YEAR);
        this.month = cal.get(java.util.Calendar.MONTH);
        this.day = cal.get(java.util.Calendar.DAY_OF_MONTH); 
    }
    
    public void setSalatTimes()
    {        
        setOptions();
        Salat prayers = new Salat();
        prayers.setCalcMethod(this.calcMethod - 1);
        prayers.setAsrMethod(this.asrMethod - 1);
        prayers.setDhuhrMinutes(0);
        prayers.setHighLatsMethod(this.highLatitude - 1);
        
        this.salaTimes = prayers.getDatePrayerTimes(year, month+1, day, this.latitude, this.longitude, this.timezone);


/*
        this.salaTimes[0] = "00:01";
        this.salaTimes[2] = "00:02";
        this.salaTimes[3] = "00:03";
        this.salaTimes[5] = "23:58";
        this.salaTimes[6] = "23:59";
*/

                
        Log.i("app", "Sataltimes : "+java.util.Arrays.asList(salaTimes).toString());
    }
    
    public String[] getSalatTimes()
    {
        return this.salaTimes;
    }    
    
    public void setHijriDate()
    {
        int[] hijriAdjustement = new int[5];
        hijriAdjustement[0] = 0;
        hijriAdjustement[1] = 1;
        hijriAdjustement[2] = 2;
        hijriAdjustement[3] = -1;
        hijriAdjustement[4] = -2;
        Hijri hijri = new Hijri();        
        this.hijriDates = hijri.isToString(year,month+1,day,hijriAdjustement[this.hijriDays - 1]);        
    }
    
    public String[] getHijriDates()
    {
        return this.hijriDates;
    }
    
    public long getTimeLeft()
    {
        long timeLeft = 0;
        if (getFajr() > 0) 
        {
            this.nextSalat = FAJR;
            //isSalat = true;
            timeLeft = getFajr();
        }
        else if (getDuhr() > 0) 
        {
            this.nextSalat = DUHR;
            //isSalat = true;
            timeLeft = getDuhr();
        }
        else if (getAsr() > 0) 
        {
            this.nextSalat = ASR;
            //isSalat = true;
            timeLeft = getAsr();
        }
        else if (getMaghrib() > 0)
        {
            this.nextSalat = MAGHRIB;
            //isSalat = true;
            timeLeft = getMaghrib();
        }
        else if (getIsha() > 0)
        {
            this.nextSalat = ISHA;
            //isSalat = true;
            timeLeft = getIsha();
        }
        else if (getMidNight() > 0)
        {  
            this.nextSalat = MIDNIGHT;
            //isSalat = false;
            timeLeft = getMidNight();
        }        
        return timeLeft;
    }
    
    public void startAlarm(Context context)
    {
        Calendar now = Calendar.getInstance();
        this.initCalendar();
        this.setSalatTimes();
        long timeToSalat = this.getTimeLeft() + now.getTimeInMillis();     

        Intent intent = new Intent(context, SalatReceiver.class);  
        //PendingIntent pendingIntent = PendingIntent.getService(context, -1, intent, PendingIntent.FLAG_UPDATE_CURRENT); 
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);    
        Log.i("app", "Next salat is " + nextSalat  + " in " + timeToSalat);

    }
  
    public void stopAlarm(Context context)
    {
        Intent intent = new Intent(context, SalatService.class); 
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.i("app", "Alarm Stopped");
    }
    
    public String getCity()
    {
        return this.city;
    }
    
    public String getCountry()
    {
        return this.country;
    }
    
    private long getFajr()
    {
        String[] times = this.salaTimes[0].split(":");
        Calendar time = Calendar.getInstance();
        time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
        long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
        Log.i("app", "fajr : "+diff);      
        return diff;
    }
    
    private long getDuhr()
    {
        String[] times = this.salaTimes[2].split(":");
        Calendar time = Calendar.getInstance();
        time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
        long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
        Log.i("app", "duhr : "+diff);   
        return diff;
    }
    
    private long getAsr()
    {
        String[] times = this.salaTimes[3].split(":");
        Calendar time = Calendar.getInstance();
        time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
        long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
        Log.i("app", "asr : "+diff);   
        return diff;
    }
    
    private long getMaghrib()
    {
        String[] times = this.salaTimes[5].split(":");
        Calendar time = Calendar.getInstance();
        time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
        long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
        Log.i("app", "maghrib : "+diff);   
        return diff;
    }
    
    private long getIsha()
    {
        String[] times = this.salaTimes[6].split(":");
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
