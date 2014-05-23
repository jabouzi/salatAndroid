package com.skanderjabouzi.salat;

import java.util.Calendar;
import android.util.Log;

import kankan.wheel.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SkanderActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.skanderjabouzi.salat.R.layout.hijri);

        //Calendar calendar = Calendar.getInstance();

        final WheelView month1 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.month1);
        final WheelView year1 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.year1);
        final WheelView day1 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.day1);
        
        final WheelView month2 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.month2);
        final WheelView year2 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.year2);
        final WheelView day2 = (WheelView) findViewById(com.skanderjabouzi.salat.R.id.day2);
        
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                //updateDays(year, month, day);
                int current = month1.getCurrentItem();
                Log.i("MONTH1", String.valueOf(current));
                Log.i("OLD ", String.valueOf(oldValue));
                Log.i("NEW ", String.valueOf(newValue));
            }
        };

        // month
        //int curMonth = calendar.get(Calendar.MONTH);
        String months1[] = {"Muharram","Safar","Rabii 1","Rabii 2","Jumada 1","Jumada 2","Rajab","Sha\'ban","Ramadhan","Shawwal","Dhul Qa\'dah","Dhul Hijjah"};
        month1.setViewAdapter(new DateArrayAdapter(this, months1, 0));
        //month.setCurrentItem(curMonth);
        month1.addChangingListener(listener);
    
        // year
        //int curYear = calendar.get(Calendar.YEAR);
        year1.setViewAdapter(new NumericWheelAdapter(this, 0, 1435));
        year1.setCurrentItem(1435);
        //year.setViewAdapter(new DateNumericAdapter(this, 0, 1435, 1435));
        //year.setCurrentItem(1435);
        year1.addChangingListener(listener);
        
        day1.setViewAdapter(new NumericWheelAdapter(this, 1, 30));
        day1.setCurrentItem(1);
        day1.addChangingListener(listener);
        
        String months2[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        month2.setViewAdapter(new DateArrayAdapter(this, months2, 0));
        //month.setCurrentItem(curMonth);
        month2.addChangingListener(listener);
    
        // year
        //int curYear = calendar.get(Calendar.YEAR);
        year2.setViewAdapter(new NumericWheelAdapter(this, 0, 1435));
        year2.setCurrentItem(1435);
        //year.setViewAdapter(new DateNumericAdapter(this, 0, 1435, 1435));
        //year.setCurrentItem(1435);
        year2.addChangingListener(listener);
        
        day2.setViewAdapter(new NumericWheelAdapter(this, 1, 30));
        day2.setCurrentItem(1);
        day2.addChangingListener(listener);
        
        //day
        //updateDays(year, month, day);
        //day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
    }
    
    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    //void updateDays(WheelView year, WheelView month, WheelView day) {
        //Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        //calendar.set(Calendar.MONTH, month.getCurrentItem());
        //
        //int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        //int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        //day.setCurrentItem(curDay - 1, true);
    //}
    
/*
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }*/
    
    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            //setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            //if (currentItem == currentValue) {
                //view.setTextColor(0xFF0000F0);
            //}
            //view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
}
