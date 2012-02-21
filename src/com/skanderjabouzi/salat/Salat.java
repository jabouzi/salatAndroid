package com.skanderjabouzi.salat;

public class Salat {
    
    private double PI;

    private int Jafari;    // Ithna Ashari
    private int Karachi;    // University of Islamic Sciences, Karachi
    private int ISNA;    // Islamic Society of North America (ISNA)
    private int MWL;    // Muslim World League (MWL)
    private int Makkah;    // Umm al-Qura, Makkah
    private int Egypt;    // Egyptian General Authority of Survey
    private int Tehran;    // Institute of Geophysics, University of Tehran
    private int Custom;    // Custom Setting

    // Juristic Methods
    private int Shafii;    // Shafii (standard)
    private int Hanafi;    // Hanafi

    // Adjusting Methods for Higher Latitudes
    private int None;    // No adjustment
    private int MidNight;    // middle of night
    private int OneSeventh;    // 1/7th of night
    private int AngleBased;    // angle/60th of night


    // Time Formats
    private int Time24;    // 24-hour format
    private int Time12;    // 12-hour format
    private int Time12NS;    // 12-hour format with no suffix
    private int Float;    // floating point number

    // Time Names
    private String InvalidTime;     // The string used for invalid times

//---------------------- Global Variables --------------------


    private int calcMethod;        // caculation method
    private double asrJuristic;        // Juristic method for Asr
    private double dhuhrMinutes;        // minutes after mid-day for Dhuhr
    private int adjustHighLats;    // adjusting method for higher latitudes

    private int timeFormat;        // time format

    private double lat;        // latitude
    private double lng;        // longitude
    private double timezone;   // time-zone
    private double JDate;      // Julian date

//--------------------- Technical Settings --------------------


    private int numIterations;        // number of iterations needed to compute times

//------------------- Calc Method Parameters --------------------

    private double methodParams[][] = new double[8][5];
    
    public Salat()
    {
        PI = 4.0*Math.atan(1.0);
        // Calculation Methods
         Jafari  = 0;    // Ithna Ashari
         Karachi = 1;    // University of Islamic Sciences, Karachi
         ISNA    = 2;    // Islamic Society of North America (ISNA)
         MWL     = 3;    // Muslim World League (MWL)
         Makkah  = 4;    // Umm al-Qura, Makkah
         Egypt   = 5;    // Egyptian General Authority of Survey
         Tehran  = 6;    // Institute of Geophysics, University of Tehran
         Custom  = 7;    // Custom Setting

         // Juristic Methods
         Shafii  = 0;    // Shafii (standard)
         Hanafi  = 1;    // Hanafi

         // Adjusting Methods for Higher Latitudes
         None    = 0;    // No adjustment
         MidNight   = 1;    // middle of night
         OneSeventh = 2;    // 1/7th of night
         AngleBased = 3;    // angle/60th of night


         // Time Formats
         Time24  = 0;    // 24-hour format
         Time12  = 1;    // 12-hour format
         Time12NS   = 2;    // 12-hour format with no suffix
         Float   = 3;    // floating point number

         // Time Names
         //timeNames = {'Fajr','Sunrise','Dhuhr','Asr','Sunset','Maghrib','Isha'};

         InvalidTime = "-----";  // The string used for invalid times


        //---------------------- Global Variables --------------------


         calcMethod   = 0;     // caculation method
         asrJuristic  = 0.0;     // Juristic method for Asr
         dhuhrMinutes = 0.0;     // minutes after mid-day for Dhuhr
         adjustHighLats = 0;    // adjusting method for higher latitudes

         timeFormat   = 0;     // time format


        //--------------------- Technical Settings --------------------


         numIterations = 1;     // number of iterations needed to compute times




        //------------------- Calc Method Parameters --------------------


         /*  methodParams[methodNum] = new Array(fa, ms, mv, is, iv);

           fa : fajr angle
           ms : maghrib selector (0 = angle; 1 = minutes after sunset)
           mv : maghrib parameter value (in angle or minutes)
           is : isha selector (0 = angle; 1 = minutes after maghrib)
           iv : isha parameter value (in angle or minutes)
         */
         methodParams[0][0] = 16.0;
         methodParams[0][1] = 0.0;
         methodParams[0][2] = 4.0;
         methodParams[0][3] = 0.0;
         methodParams[0][4] = 14.0;

         methodParams[1][0] = 18.0;
         methodParams[1][1] = 1.0;
         methodParams[1][2] = 0.0;
         methodParams[1][3] = 0.0;
         methodParams[1][4] = 18.0;

         methodParams[2][0] = 15.0;
         methodParams[2][1] = 1.0;
         methodParams[2][2] = 0.0;
         methodParams[2][3] = 0.0;
         methodParams[2][4] = 15.0;

         methodParams[3][0] = 18.0;
         methodParams[3][1] = 1.0;
         methodParams[3][2] = 0.0;
         methodParams[3][3] = 0.0;
         methodParams[3][4] = 17.0;

         methodParams[4][0] = 19.0;
         methodParams[4][1] = 1.0;
         methodParams[4][2] = 0.0;
         methodParams[4][3] = 1.0;
         methodParams[4][4] = 90.0;

         methodParams[5][0] = 19.5;
         methodParams[5][1] = 1.0;
         methodParams[5][2] = 0.0;
         methodParams[5][3] = 0.0;
         methodParams[5][4] = 17.5;

         methodParams[6][0] = 18.0;
         methodParams[6][1] = 1.0;
         methodParams[6][2] = 0.0;
         methodParams[6][3] = 0.0;
         methodParams[6][4] = 17.0;

         methodParams[7][0] = 17.7;
         methodParams[7][1] = 0.0;
         methodParams[7][2] = 4.5;
         methodParams[7][3] = 0.0;
         methodParams[7][4] = 15.0;     
    }

    // return prayer times for a given date
    public String[]  getDatePrayerTimes(int year, int month, int day, double latitude, double longitude, double timeZone)
    {
        lat = latitude;
        lng = longitude;
        timezone = timeZone;
        //timeZone = effectiveTimeZone(year, month, day, timeZone);
        JDate = julianDate(year, month, day)- longitude/ (15* 24);    
        return computeDayTimes();
    }

    // set the calculation method
    public void  setCalcMethod(int methodID)
    {
        calcMethod = methodID;
    }

    // set the juristic method for Asr
    public void  setAsrMethod(int methodID)
    {
        if (methodID < 0 || methodID > 1)
         return;
        asrJuristic = methodID;
    }

    // set the angle for calculating Fajr
    public void  setFajrAngle(double angle)
    {
        double customParams[] = {angle, -1, -1, -1, -1};
        setCustomParams(customParams);
    }

    // set the angle for calculating Maghrib
    public void  setMaghribAngle(double angle)
    {
        double customParams[] = {-1, 0, angle, -1, -1};
        setCustomParams(customParams);
    }

    // set the angle for calculating Isha
    public void  setIshaAngle(double angle)
    {
        double customParams[] = {-1, -1, -1, 0, angle};
        setCustomParams(customParams);
    }

    // set the minutes after mid-day for calculating Dhuhr
    public void  setDhuhrMinutes(int minutes)
    {
        dhuhrMinutes = minutes;
    }

    // set the minutes after Sunset for calculating Maghrib
    public void  setMaghribMinutes(int minutes)
    {
        double customParams[] = {-1, 1, minutes, -1, -1};
        setCustomParams(customParams);
    }

    // set the minutes after Maghrib for calculating Isha
    public void  setIshaMinutes(int minutes)
    {
        double customParams[] = {-1, 1, minutes, -1, -1};
        setCustomParams(customParams);
    }

    // set custom values for calculation parameters
    public void  setCustomParams(double params[])
    {
        for (int i=0; i<5; i++)
        {
         if (params[i] == -1)
          methodParams[Custom][i] = methodParams[calcMethod][i];
         else
          methodParams[Custom][i] = params[i];
        }
        calcMethod = Custom;
    }

    // set adjusting method for higher latitudes
    public void  setHighLatsMethod(int methodID)
    {
        adjustHighLats = methodID;
    }

    // set the time format
    public void  setTimeFormat(int timeFormat_)
    {
        timeFormat = timeFormat_;
    }

    // convert float hours to 24h format
    public String  floatToTime24(double time)
    {
        if (Double.isNaN(time))
         return InvalidTime;
        else{
         time = fixhour(time+ 0.5/ 60);  // add 0.5 minutes to round
         double hours = Math.floor(time);
         double minutes = Math.floor((time- hours)* 60);
         return twoDigitsFormat((int)hours)+':'+ twoDigitsFormat((int)minutes);
        }
    }

    // convert float hours to 12h format
    public String  floatToTime12(double time)
    {
        if (Double.isNaN(time))
         return InvalidTime;
        else{
         time = fixhour(time+ 0.5/ 60);  // add 0.5 minutes to round
         int hours = ((int)time);
         int minutes = (((int)time- hours)* 60);
         String suffix = hours >= 12.0 ? " pm" : " am";
         hours = (hours + 12 - 1) % 12 + 1;
         return Integer.toString(hours)+':'+ twoDigitsFormat(minutes)+ suffix;
        }
    }


    //---------------------- Calculation Functions -----------------------

    // References:
    // http://www.ummah.net/astronomy/saltime
    // http://aa.usno.navy.mil/faq/docs/SunApprox.html


    // compute declination angle of sun and equation of time
    public double[]  sunPosition(double jd)
    {
        double D = jd - 2451545.0;
        double g = fixangle(357.529 + 0.98560028* D);
        double q = fixangle(280.459 + 0.98564736* D);
        double L = fixangle(q + 1.915* dsin(g) + 0.020* dsin(2*g));

        //double R = 1.00014 - 0.01671* dcos(g) - 0.00014* dcos(2*g);
        double e = 23.439 - 0.00000036* D;

        double d = darcsin(dsin(e)* dsin(L));
        double RA = darctan2(dcos(e)* dsin(L), dcos(L))/ 15;
        RA = fixhour(RA);
        double EqT = q/15 - RA;
        double result[] = new double[2];
        result[0]= d;
        result[1]= EqT;
        return result;
    }

    // compute equation of time
    public double  equationOfTime(double jd)
    {
        return sunPosition(jd)[1];
    }

    // compute declination angle of sun
    public double  sunDeclination(double jd)
    {
        return sunPosition(jd)[0];
    }

    // compute mid-day (Dhuhr, Zawal) time
    public double  computeMidDay(double t)
    {
        double T = equationOfTime(JDate + t);
        double Z = fixhour(12 - T);
        return Z;
    }

    // compute time for a given angle G
    public double  computeTime(double G, double t)
    {
        double D = sunDeclination(JDate + t);
        double Z = computeMidDay(t);
        double V = 1.0/15.0* darccos((-dsin(G)- dsin(D)* dsin(lat)) /
          (dcos(D)* dcos(lat)));
        return Z + (G > 90.0 ? -V : V);
    }

    // compute the time of Asr
    public double  computeAsr(int step, double t)  // Shafii: step=1, Hanafi: step=2
    {
        double D = sunDeclination(JDate+ t);
        double G = -darccot(step+ dtan(Math.abs(lat-D)));
        return computeTime(G, t);
    }


    //---------------------- Compute Prayer Times -----------------------


    // compute prayer times at given julian date
    public double[] computeTimes(double[] times)
    {
        double t[] = dayPortion(times);
        double Fajr    = computeTime(180.0 - methodParams[calcMethod][0], t[0]);
        double Sunrise = computeTime(180.0 - 0.833, t[1]);
        double Dhuhr   = computeMidDay(t[2]);
        double Asr  = computeAsr((int)(1.0 + asrJuristic), t[3]);
        double Sunset  = computeTime(0.833, t[4]);;
        double Maghrib = computeTime(methodParams[calcMethod][2], t[5]);
        double Isha    = computeTime(methodParams[calcMethod][4], t[6]);
        double salats[] = new double[7];
        salats[0] = Fajr;
        salats[1] = Sunrise;
        salats[2] = Dhuhr;
        salats[3] = Asr;
        salats[4] = Sunset;
        salats[5] = Maghrib;
        salats[6] = Isha;
        return salats;
    }


    // compute prayer times at given julian date
    public String[]  computeDayTimes()
    {
        double times[] = new double[7];
        times[0] = 5.0;
        times[1] = 6.0;
        times[2] = 12.0;
        times[3] = 13.0;
        times[4] = 18.0;
        times[5] = 18.0;
        times[6] = 18.0; //default times

        for (int i=1; i<=numIterations; i++)
        {
            times = computeTimes(times);
        }
            times = adjustTimes(times);
        return adjustTimesFormat(times);
    }


    // adjust times in a prayer time array
    public double[]  adjustTimes(double[] times)
    {
        for (int i=0; i<7; i++)
         times[i] += timezone - lng/15.0;
         times[2] += dhuhrMinutes/ 60.0; //Dhuhr
        if (methodParams[calcMethod][1] == 1) // Maghrib
         times[5] = times[4]+ methodParams[calcMethod][2]/ 60.0;
        if (methodParams[calcMethod][3] == 1) // Isha
         times[6] = times[5]+ methodParams[calcMethod][4]/ 60.0;
        
        if (adjustHighLats != None)    
         times = adjustHighLatTimes(times);

        return times;
    }


    // convert times array to given time format
    public String[]  adjustTimesFormat(double[] times)
    {
        /*if (timeFormat == Float)
         return times;*/
        String timesF[] = new String[7];
        for (int i=0; i<7; i++)
         if (timeFormat == Time12)
          timesF[i] = floatToTime12(times[i]);
         /*else if (timeFormat == Time12NS)
          timesF[i] = floatToTime12(times[i], true);*/
         else
          timesF[i] = floatToTime24(times[i]);
        return timesF;
    }


    // adjust Fajr, Isha and Maghrib for locations in higher latitudes
    public double[]  adjustHighLatTimes(double[] times)
    {
        double nightTime = timeDiff(times[4], times[1]); // sunset to sunrise

        // Adjust Fajr
        double FajrDiff = nightPortion(methodParams[calcMethod][0])* nightTime;
        if (Double.isNaN(times[0]) || timeDiff(times[0], times[1]) > FajrDiff)
         times[0] = times[1]- FajrDiff;

        // Adjust Isha
        double IshaAngle = (methodParams[calcMethod][3] == 0) ? methodParams[calcMethod][4] : 18;
        double IshaDiff = nightPortion(IshaAngle)* nightTime;
        if (Double.isNaN(times[6]) || timeDiff(times[4], times[6]) > IshaDiff)
         times[6] = times[4]+ IshaDiff;

        // Adjust Maghrib
        double MaghribAngle = (methodParams[calcMethod][1] == 0) ? methodParams[calcMethod][2] : 4;
        double MaghribDiff = nightPortion(MaghribAngle)* nightTime;
        if (Double.isNaN(times[5]) || timeDiff(times[4], times[5]) > MaghribDiff)
         times[5] = times[4]+ MaghribDiff;

        return times;
    }


    // the night portion used for adjusting times in higher latitudes
    public double  nightPortion(double angle)
    {
        double result = 0.0;
        if (adjustHighLats == AngleBased)
         result = 1.0/60.0* angle;
        if (adjustHighLats == MidNight)
         result = 1.0/2.0;
        if (adjustHighLats == OneSeventh)
         result = 1.0/7.0;
        return result;
    }


    // convert hours to day portions
    public double[]  dayPortion(double[] times)
    {
        for (int i=0; i<7; i++)
         times[i] /= 24;
        return times;
    }



    //---------------------- Misc Functions -----------------------


    // compute the difference between two times
    public double  timeDiff(double time1, double time2)
    {
        return fixhour(time2- time1);
    }


    // add a leading 0 if necessary
    public String  twoDigitsFormat(int num)
    {
        return (num <10) ? '0'+ Integer.toString(num) : Integer.toString(num);
    }

    //---------------------- Julian Date Functions -----------------------


    // calculate julian date from a calendar date
    public double  julianDate(int year, int month, int day)
    {
        if (month <= 2)
        {
         year -= 1;
         month += 12;
        }
        double A = Math.floor(year/ 100.0);
        double B = 2.0 - A+ Math.floor(A/ 4.0);

        double JD = Math.floor(365.25* (year+ 4716))+ Math.floor(30.6001* (month+ 1))+ day+ B- 1524.5;
        return JD;
    }

    //---------------------- Trigonometric Functions -----------------------

    // degree sin
    public double  dsin(double d)
    {
        return Math.sin(dtr(d));
    }

    // degree cos
    public double  dcos(double d)
    {
        return Math.cos(dtr(d));
    }

    // degree tan
    public double  dtan(double d)
    {
        return Math.tan(dtr(d));
    }

    // degree arcsin
    public double  darcsin(double x)
    {
        return rtd(Math.asin(x));
    }

    // degree arccos
    public double  darccos(double x)
    {
        return rtd(Math.acos(x));
    }

    // degree arctan
    public double  darctan(double x)
    {
        return rtd(Math.atan(x));
    }

    // degree arctan2
    public double  darctan2(double y, double x)
    {
        return rtd(Math.atan2(y, x));
    }

    // degree arccot
    public double  darccot(double x)
    {
        return rtd(Math.atan(1/x));
    }

    // degree to radian
    public double  dtr(double d)
    {
        return (d * PI) / 180.0;
    }

    // radian to degree
    public double  rtd(double r)
    {
        return (r * 180.0) / PI;
    }

    // range reduce angle in degrees.
    public double  fixangle(double a)
    {
        a = a - 360.0 * (Math.floor(a / 360.0));
        a = a < 0 ? a + 360.0 : a;
        return a;
    }

    // range reduce hours to 0..23
    public double  fixhour(double a)
    {
        a = a - 24.0 * (Math.floor(a / 24.0));
        a = a < 0 ? a + 24.0 : a;
        return a;
    }
}
