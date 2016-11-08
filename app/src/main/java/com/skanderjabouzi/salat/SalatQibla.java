package com.skanderjabouzi.salat;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.text.Html;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.animation.ObjectAnimator;
import java.util.ArrayList;
import android.animation.AnimatorSet;

public class SalatQibla extends Activity implements SensorEventListener {

	private View qiblaLayout;
	private ImageView image;
	private ImageView image2;
	private int sensorAccuracy;
	private float currentDegree = 0f;
	private SensorManager mSensorManager;
	TextView compassDegree;
	TextView compassDegreeTitle;
	TextView qiblaDegree;	
	TextView qiblaDegreeTitle;	
	private LocationDataSource datasource;
	private Location location;
	private boolean background_changed = false;
	private Context context = SalatQibla.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qibla);
		qiblaLayout = findViewById(R.id.qibla_bg);
		image = (ImageView) findViewById(R.id.compass);
		image2 = (ImageView) findViewById(R.id.compass2);
		compassDegree = (TextView) findViewById(R.id.degree);
		compassDegreeTitle = (TextView) findViewById(R.id.degree_title);
		qiblaDegree = (TextView) findViewById(R.id.qibla_degree);
		qiblaDegreeTitle = (TextView) findViewById(R.id.qibla_degree_title);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		datasource = new LocationDataSource(this);
		datasource.open();
		location = datasource.getLocation(1);
		compassDegreeTitle.setText(this.getString(R.string.titleDegree));
		qiblaDegreeTitle.setText(this.getString(R.string.titleQiblaDegree));
		qiblaDegree.setText(String.format("%d",(int)getQibla()));
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        if (datasource.isOpen()) datasource.close();
    }
    
	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (datasource.isOpen()) datasource.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
	public void onSensorChanged(SensorEvent event) {
		float degree = Math.round(event.values[0]);

		if ((int)degree == (int)getQibla())
		{
			background_changed = true;
			qiblaLayout.setBackgroundResource(R.drawable.bg2);
		}
		else
		{
			if (background_changed)
			{
				background_changed = false;
				qiblaLayout.setBackgroundResource(R.drawable.bg1);
			}
		}
		
		compassDegree.setText(String.format("%d",(int)degree));
		rotate(image, currentDegree, degree, getQibla(), 300);
		currentDegree = -degree;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		sensorAccuracy = accuracy;
		Log.d("SENSOR : ", String.valueOf(sensorAccuracy));
	}

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void rotate(ImageView imgview, float currentDegree, float degree, float qibla, int duration) {

		ArrayList<ObjectAnimator> arrayListObjectAnimators = new ArrayList<ObjectAnimator>();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(image , "rotation", currentDegree, -degree);
        arrayListObjectAnimators.add(anim1);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(image2 , "rotation", currentDegree + qibla, -degree + qibla);
        arrayListObjectAnimators.add(anim2);

        ObjectAnimator[] objectAnimators = arrayListObjectAnimators.toArray(new ObjectAnimator[arrayListObjectAnimators.size()]);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(objectAnimators);
        animSetXY.setDuration((long) duration);
        animSetXY.start();
	}

	private float getQibla()
	{    
		final float MLONG = 39.823333f;
		final float MLAT = 21.42333f;    

		float x1 = (float)Math.sin((-location.getLongitude()+MLONG)*Math.PI/180f);
		float y1 = (float)Math.cos(location.getLatitude()*Math.PI/180f) * (float)Math.tan(MLAT*Math.PI/180f);
		float y2 = (float)Math.sin(location.getLatitude()*Math.PI/180f) * (float)Math.cos((-location.getLongitude()+MLONG)*Math.PI/180f);
		float qibla_angle = (float)Math.atan(x1/(y1-y2))*180f/(float)Math.PI;
		if (qibla_angle < 0) qibla_angle = 360.0f + qibla_angle;
		
		if ((location.getLongitude() < MLONG) && (location.getLongitude() > MLONG-180f)) {
			if (qibla_angle > 180f) qibla_angle = qibla_angle - 180f;
		}
		if (qibla_angle > 360f) qibla_angle = qibla_angle - 360f;    
		return qibla_angle;        
	}
}
