package com.skanderjabouzi.salat;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.Html;
import android.util.Log;

public class SalatQibla extends Activity implements SensorEventListener {

	private ImageView image;
	private ImageView image2;
	private int sensorAccuracy;
	private float currentDegree = 0f;
	//private float currentDegree2 = 178f;
	private SensorManager mSensorManager;
	TextView compassDegree;
	TextView compassDegreeTitle;
	TextView qiblaDegree;	
	TextView qiblaDegreeTitle;	
	private LocationDataSource datasource;
	private Location location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qibla);
		image = (ImageView) findViewById(R.id.compass);
		image2 = (ImageView) findViewById(R.id.compass2);
		//rotate(image2, 0, 178f, 0);
		compassDegree = (TextView) findViewById(R.id.degree);
		compassDegreeTitle = (TextView) findViewById(R.id.degree_title);
		qiblaDegree = (TextView) findViewById(R.id.qibla_degree);
		qiblaDegreeTitle = (TextView) findViewById(R.id.qibla_degree_title);
		//qiblaDegree.setText(Float.toString(58.64f));
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
	public void onSensorChanged(SensorEvent event) {

		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);
		compassDegree.setText(String.format("%d",(int)degree));
		//degree = degree - 178f;
		//qiblaDegree.setText(Float.toString(degree));

		// create a rotation animation (reverse turn degree degrees)
		//RotateAnimation ra = new RotateAnimation(
				//currentDegree, 
				//-degree,
				//Animation.RELATIVE_TO_SELF, 0.5f, 
				//Animation.RELATIVE_TO_SELF,
				//0.5f);

		// how long the animation will take place
		//ra.setDuration(300);

		// set the animation after the end of the reservation status
		//ra.setFillAfter(true);

		// Start the animation
		//image.startAnimation(ra);
		//float degree2 = degree - 178f;

		rotate(image, currentDegree, degree, 300);
		//rotate(image2, currentDegree2, degree2, 300);
		
		//image2.startAnimation(ra);
		currentDegree = -degree;
		//currentDegree2 = -degree;

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		sensorAccuracy = accuracy;
		Log.d("SENSOR : ", String.valueOf(sensorAccuracy));
	}
	
	private void rotate(ImageView imgview, float currentDegree, float degree, int duration) {
		RotateAnimation rotateAnim = new RotateAnimation(currentDegree, -degree,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);

		rotateAnim.setDuration(duration);
		rotateAnim.setFillAfter(true);
		imgview.startAnimation(rotateAnim);
	}
	
	private float getQibla()
	{    
		final float MLONG = 39.823333f;
		final float MLAT = 21.42333f;    
		//final float Math.PI = 4.0f*Math.atan(1.0f);
		
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
