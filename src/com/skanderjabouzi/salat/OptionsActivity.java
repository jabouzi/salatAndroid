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

public class OptionsActivity extends Activity implements OnItemSelectedListener{

	private Spinner method, asr, hijri, highLatitudes;
	private Button btnSaveOptions;
	private OptionsDataSource datasource;
	private Options options;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        addListenerOnButton();
		addListenerOnSpinnerItemSelection();
		datasource = new OptionsDataSource(this);
		datasource.open();
		options = datasource.getOptions(1);
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
    
    public void addListenerOnSpinnerItemSelection() {
		method = (Spinner) findViewById(R.id.calculation);
		method.setOnItemSelectedListener(this);
		method.setSelection(options.getMethod());
		
		asr = (Spinner) findViewById(R.id.asr);
		asr.setOnItemSelectedListener(this);
		asr.setSelection(options.getAsr());
		
		hijri = (Spinner) findViewById(R.id.hijri);
		hijri.setOnItemSelectedListener(this);
		hijri.setSelection(options.getHijri());
		
		highLatitudes = (Spinner) findViewById(R.id.highLatitudes);
		highLatitudes.setOnItemSelectedListener(this);
		highLatitudes.setSelection(options.getHigherLatitude());
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Toast.makeText(parent.getContext(), 
				"OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
				Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

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
				
				//Options options = new Options();
				options.setId(1);
				options.setMethod(Integer.parseInt(String.valueOf(method.getSelectedItem())));
				options.setAsr(Integer.parseInt(String.valueOf(asr.getSelectedItem())));
				options.setHijri(Integer.parseInt(String.valueOf(hijri.getSelectedItem())));
				options.setHigherLatitude(Integer.parseInt(String.valueOf(highLatitudes.getSelectedItem())));
				datasource.updateOptions(options);
				
				Toast.makeText(OptionsActivity.this,
						"OnClickListener : " + 
						"\nSpinner 1 : " + String.valueOf(method.getSelectedItem()) +
						"\nSpinner 2 : " + String.valueOf(asr.getSelectedItem()) +
						"\nSpinner 3 : " + String.valueOf(hijri.getSelectedItem()) +
						"\nSpinner 4 : " + String.valueOf(highLatitudes.getSelectedItem()),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
