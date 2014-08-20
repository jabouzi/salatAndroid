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
import android.util.Log;

public class SalatQibla extends Activity implements SensorEventListener {

	// define the display assembly compass picture
	private ImageView image;
	private ImageView image2;
	private int sensorAccuracy;

	// record the compass picture angle turned
	private float currentDegree = 0f;

	// device sensor manager
	private SensorManager mSensorManager;

	TextView compassDegree;
	TextView qiblaDegree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qibla);
		image = (ImageView) findViewById(R.id.compass);

		// our compass image
		image2 = (ImageView) findViewById(R.id.compass2);
		//rotate(image2, 0, 178f, 0);
		
		// TextView that will tell the user what degree is he heading
		compassDegree = (TextView) findViewById(R.id.degree);
		qiblaDegree = (TextView) findViewById(R.id.qibla_degree);
		//qiblaDegree.setText(Float.toString(58.64f));

		// initialize your android device sensor capabilities
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// for the system's orientation sensor registered listeners
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		// to stop the listener and save battery
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);
		compassDegree.setText(Float.toString(degree));
		//degree = degree - 178f;
		qiblaDegree.setText(Float.toString(degree));

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
		//float degree2 = degree + 178f;
		rotate(image, currentDegree, degree, 300);
		rotate(image2, currentDegree, degree, 300);
		
		//image2.startAnimation(ra);
		currentDegree = -degree;

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// not in use
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
}
