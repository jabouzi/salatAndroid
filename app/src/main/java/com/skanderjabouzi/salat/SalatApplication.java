package com.skanderjabouzi.salat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Arrays;

import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.AlarmManager;

import java.io.File;
import java.io.BufferedWriter;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
import android.os.Environment;
import java.io.FileWriter;

public class SalatApplication{

	private static final String TAG = "SALATAPPLICATION";
	public static final int FAJR = 0;
	public static final int DUHR = 2;
	public static final int ASR = 3;
	public static final int MAGHRIB = 5;
	public static final int ISHA = 6;
	public static final int MIDNIGHT = 7;

	public static  String[] salatNames = new String[7];
	private static String salaTimes[] = new String[7];
	private static String[] hijriDates = new String[4];

	private int year;
	private int month;
	private int day;
	private int calcMethod;
	private int asrMethod;
	private int hijriDays;
	private int highLatitude;
	private int adhan;
	private int autoLocation;
	private float latitude;
	private float longitude;
	private float timezone;
	private String city;
	private String country;

	public static int nextSalat;
	public static int prefType;

	private OptionsDataSource optionsDataSource;
	private LocationDataSource locationDataSource;
	private Options salatOptions;
	private Location salatLocation;
	
	private static SalatApplication instance = null;


	protected SalatApplication(Context context) {
		salatNames[0] = "Fajr";
		salatNames[2] = "Duhr";
		salatNames[3] = "Asr";
		salatNames[5] = "Maghrib";
		salatNames[6] = "Isha";
		optionsDataSource = new OptionsDataSource(context);
		locationDataSource = new LocationDataSource(context);
		setOptions();
		Log.i("Salat app", "Constructor");
	}
	

	public static SalatApplication getInstance(Context context) {
		if(instance == null) {
			instance = new SalatApplication(context);
		}
		return instance;
	}

	public void setOptions()
	{
		this.optionsDataSource.open();
		this.salatOptions = optionsDataSource.getOptions(1);
		this.locationDataSource.open();
		this.salatLocation = locationDataSource.getLocation(1);
		this.calcMethod = salatOptions.getMethod();
		this.asrMethod = salatOptions.getAsr();
		this.hijriDays = salatOptions.getHijri();
		this.highLatitude = salatOptions.getHigherLatitude();
		this.adhan = salatOptions.getAdhan();
		this.autoLocation = salatOptions.getAutoLocation();
		this.latitude = salatLocation.getLatitude();
		this.longitude = salatLocation.getLongitude();
		this.timezone = salatLocation.getTimezone();
		this.city = salatLocation.getCity();
		this.country = salatLocation.getCountry();
		this.locationDataSource.close();
		this.optionsDataSource.close();
		    
		Log.i("Salat app", "Calculation " + calcMethod + " " + asrMethod + " " + hijriDays + " " + highLatitude + " " + adhan + " " + autoLocation);
		Log.i("Salat app", "Location " + String.valueOf(longitude) + " " + String.valueOf(latitude) + " " + String.valueOf(timezone));
	}

	public boolean checkOptions()
	{
		if (this.calcMethod == 0)
		{
			Log.i("Salat app", "CHECK CALC 0");
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

	public void setSalatTimes(int nextDay)
	{
		setOptions();
		Salat prayers = new Salat();
		prayers.setCalcMethod(this.calcMethod - 1);
		prayers.setAsrMethod(this.asrMethod - 1);
		prayers.setDhuhrMinutes(0);
		prayers.setHighLatsMethod(this.highLatitude - 1);

		this.salaTimes = prayers.getDatePrayerTimes(year, month+1, day+nextDay, this.latitude, this.longitude, this.timezone);

		//this.salaTimes[0] = "00:05";
		//this.salaTimes[2] = "23:40";
		//this.salaTimes[3] = "23:45";
		//this.salaTimes[5] = "23:50";
		//this.salaTimes[6] = "23:55";

		Log.i("Salat app", "setSalatTimes : "+java.util.Arrays.asList(salaTimes).toString());
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
		this.hijriDates = hijri.isToString(year,month+1,day,hijriAdjustement[this.hijriDays]);
		//int[] hijris = new int[3];
		//hijris = hijri.chrToIsl(year,month+1,day,hijriAdjustement[this.hijriDays]);
		//Log.i("Salat app", "CH2IS : ["+hijris[0]+", "+hijris[1]+", "+hijris[2]+"]");
		//hijris = hijri.islToChr(+hijris[2],+hijris[1],+hijris[0],hijriAdjustement[this.hijriDays]);
		//Log.i("Salat app", "IS2CH : ["+hijris[0]+", "+hijris[1]+", "+hijris[2]+"]");
		//Log.i("Salat app", "setHijriDate : "+java.util.Arrays.asList(hijriDates).toString());
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
			timeLeft = getFajr();
		}
		else if (getDuhr() > 0)
		{
			this.nextSalat = DUHR;
			timeLeft = getDuhr();
		}
		else if (getAsr() > 0)
		{
			this.nextSalat = ASR;
			timeLeft = getAsr();
		}
		else if (getMaghrib() > 0)
		{
			this.nextSalat = MAGHRIB;
			timeLeft = getMaghrib();
		}
		else if (getIsha() > 0)
		{
			this.nextSalat = ISHA;
			timeLeft = getIsha();
		}
		else if (getMidNight() > 0)
		{
			this.nextSalat = MIDNIGHT;
			timeLeft = getMidNight();
		}
		
		Log.d("Salat NEXT", String.valueOf(nextSalat));
		SalatApplication.write2sd("Salat NEXT", String.valueOf(nextSalat));
		return timeLeft;
	}

	public long getTimeToSalat()
	{
		Calendar now = Calendar.getInstance();
		initCalendar();
		setSalatTimes(0);
		return this.getTimeLeft() + now.getTimeInMillis();
	}

	public String getCity()
	{
		return this.city;
	}

	public String getCountry()
	{
		return this.country;
	}
	
	public int getAdhan()
	{
		return this.adhan;
	}
	
	public int getAutoLocation()
	{
		return this.autoLocation;
	}

	private long getFajr()
	{
		String[] times = this.salaTimes[0].split(":");
		Calendar time = Calendar.getInstance();
		time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
		long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
		Log.i("Salat app", "fajr : "+diff);
		return diff;
	}

	private long getDuhr()
	{
		String[] times = this.salaTimes[2].split(":");
		Calendar time = Calendar.getInstance();
		time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
		long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
		Log.i("Salat app", "duhr : "+diff);
		return diff;
	}

	private long getAsr()
	{
		String[] times = this.salaTimes[3].split(":");
		Calendar time = Calendar.getInstance();
		time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
		long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
		Log.i("Salat app", "asr : "+diff);
		return diff;
	}

	private long getMaghrib()
	{
		String[] times = this.salaTimes[5].split(":");
		Calendar time = Calendar.getInstance();
		time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
		long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
		Log.i("Salat app", "maghrib : "+diff);
		return diff;
	}

	private long getIsha()
	{
		String[] times = this.salaTimes[6].split(":");
		Calendar time = Calendar.getInstance();
		time.set(year, month, day, Integer.parseInt(times[0]), Integer.parseInt(times[1]),0);
		long diff = getTimeInMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]),1) - getCurrentTimeInMS();
		Log.i("Salat app", "isha : "+diff);
		return diff;
	}

	private long getMidNight()
	{
		Calendar time = Calendar.getInstance();
		time.set(year, month, day, 24, 0);
		long diff = getTimeInMS(24,0,1) - getCurrentTimeInMS();
		Log.i("Salat app", "midnight : "+diff);
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
	
	public static void setAlarm(Context context, String source) {
		SalatApplication salatApp = SalatApplication.getInstance(context);
		salatApp.cancelAlarm(context, source);
		long timeToSalat = salatApp.getTimeToSalat();
		Intent adhanIntent = new Intent(context, AdhanService.class);
		Log.i("SalatApplication", "Next salat is " + salatApp.nextSalat  + " / " + SalatApplication.nextSalat);
		if (SalatApplication.nextSalat == 7) adhanIntent.putExtra("TIME", "00:00");
		else adhanIntent.putExtra("TIME", salaTimes[SalatApplication.nextSalat]);
		adhanIntent.putExtra("NEXT", String.valueOf(SalatApplication.nextSalat));
		if (SalatApplication.nextSalat < 7) adhanIntent.putExtra("NAME", salatApp.salatNames[SalatApplication.nextSalat]);
		else adhanIntent.putExtra("NAME", "N/A");
		adhanIntent.putExtra("ADHAN", String.valueOf(salatApp.getAdhan()));
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, adhanIntent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);
		Log.i("SalatApplication", "Next salat is " + SalatApplication.nextSalat  + " in " + timeToSalat);
		Log.i("SalatApplication", "setAlarm - Source " + source);
	}
	
	public static void cancelAlarm(Context context, String source) {
		Intent adhanIntent = new Intent(context, AdhanService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, adhanIntent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();
		Log.i("SalatApplication", "cancelAlarm - Source " + source);
	}
	
	public static void write2sd(String tag, String data)
	{
		try
		{
			long timeMS = Calendar.getInstance().getTimeInMillis();
			String currentTime = new SimpleDateFormat("MM-dd HH:mm:ss").format(timeMS);
			File root = new File(Environment.getExternalStorageDirectory(), "Salat");

			if (!root.exists()) {
				root.mkdirs();

			}

			File gpxfile = new File(root, "salat.txt");

			BufferedWriter bW;

			bW = new BufferedWriter(new FileWriter(gpxfile, true));
			bW.write(currentTime + " - " + tag + " - " + data);
			bW.newLine();
			bW.flush();
			bW.close();
		}
		catch(IOException e)
		{
			Log.i("SalatApplication", e.getMessage());
		}
	}
}
