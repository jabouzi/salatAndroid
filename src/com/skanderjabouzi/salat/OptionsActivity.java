package com.skanderjabouzi.salat;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Context;
import android.util.Log;

public class OptionsActivity extends Activity implements OnItemSelectedListener{

	private Spinner method, asr, hijri, highLatitudes;
	private Button btnSaveOptions;
	private OptionsDataSource datasource;
	private Options options;
	private int pos = 0;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
		datasource = new OptionsDataSource(this);
		datasource.open();
		options = datasource.getOptions(1);
		setSpinnerItemSelection();
		addListenerOnButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        datasource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        datasource.close();
    }

    public void setSpinnerItemSelection() {
		
		method = (Spinner) findViewById(R.id.calculation);
		method.setOnItemSelectedListener(this);
		pos = options.getMethod() - 1;
		if (pos < 0) pos = 0;
		method.setSelection(pos);

		asr = (Spinner) findViewById(R.id.asr);
		asr.setOnItemSelectedListener(this);
		pos = options.getAsr() - 1;
		if (pos < 0) pos = 0;
		asr.setSelection(pos);

		hijri = (Spinner) findViewById(R.id.hijri);
		hijri.setOnItemSelectedListener(this);
		pos = options.getHijri() - 1;
		if (pos < 0) pos = 0;
		hijri.setSelection(pos);

		highLatitudes = (Spinner) findViewById(R.id.highLatitudes);
		highLatitudes.setOnItemSelectedListener(this);
		pos = options.getHigherLatitude() - 1;
		if (pos < 0) pos = 0;
		highLatitudes.setSelection(pos);
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	public void addListenerOnButton() {

		method = (Spinner) findViewById(R.id.calculation);
		asr = (Spinner) findViewById(R.id.asr);
		hijri = (Spinner) findViewById(R.id.hijri);
		highLatitudes = (Spinner) findViewById(R.id.highLatitudes);
		
		btnSaveOptions = (Button) findViewById(R.id.saveOptions);

		btnSaveOptions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				options.setId(1);
				pos = method.getSelectedItemPosition() + 1;
				options.setMethod(pos);				
				pos = asr.getSelectedItemPosition() + 1;
				options.setAsr(pos);				
				pos = hijri.getSelectedItemPosition() + 1;
				options.setHijri(pos);				
				pos = highLatitudes.getSelectedItemPosition() + 1;
				options.setHigherLatitude(pos);

				datasource.updateOptions(options);
				//salatApp.setOptions(salatOptions, salatLocation);
				//long timeToSalat = salatApp.getTimeToSalat();
				//Intent athanIntent = new Intent(context, SalatReceiver.class);
				//PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, athanIntent, 0);
				//AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				//alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSalat, pendingIntent);
			}
		});
	}

}
