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
import android.view.View;
import android.text.Html;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Canvas;

public class SalatQibla extends Activity implements SensorEventListener {

	private View qiblaLayout;
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
	private boolean background_changed = false;
	private Context context = SalatQibla.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//startService(new Intent(context, LocationService.class));
		setContentView(R.layout.qibla);
		qiblaLayout = findViewById(R.id.qibla_bg);
		image = (ImageView) findViewById(R.id.compass);
//        image.setImageBitmap(initImage());
//		image2 = (ImageView) findViewById(R.id.compass2);
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
        //finish();
        mSensorManager.unregisterListener(this);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        if (datasource.isOpen()) datasource.close();
        //finish();
        //mSensorManager.unregisterListener(this);
    }
    
	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (datasource.isOpen()) datasource.close();
        //finish();
        //mSensorManager.unregisterListener(this);
    }

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
		rotate(image, currentDegree, degree, 300);
		currentDegree = -degree;
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

        Log.d("LNG : ", String.valueOf(location.getLongitude()));
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

	private Bitmap initImage()
	{
		Bitmap bmpOriginal = BitmapFactory.decodeResource(this.getResources(), R.drawable.arrow);
		Bitmap targetBitmap = Bitmap.createBitmap((bmpOriginal.getWidth()),	(bmpOriginal.getHeight()), Bitmap.Config.ARGB_8888);
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setDither(true);
		p.setFilterBitmap(true);

		Matrix matrix = new Matrix();
		matrix.setRotate((float) getQibla(),(float) (bmpOriginal.getWidth()/2),
				(float)(bmpOriginal.getHeight()/2));

		RectF rectF = new RectF(0, 0, bmpOriginal.getWidth(), bmpOriginal.getHeight());
		matrix.mapRect(rectF);

		targetBitmap = Bitmap.createBitmap((int)rectF.width(), (int)rectF.height(), Bitmap.Config.ARGB_8888);


		Canvas tempCanvas = new Canvas(targetBitmap);
		tempCanvas.drawBitmap(bmpOriginal, matrix, p);

        Bitmap bmOverlay = Bitmap.createBitmap(targetBitmap.getWidth(), targetBitmap.getHeight(), targetBitmap.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(targetBitmap, new Matrix(), null);
        Bitmap bmp2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.cadran);
        canvas.drawBitmap(bmp2, 0, 0, null);

        return bmOverlay;
	}
}
