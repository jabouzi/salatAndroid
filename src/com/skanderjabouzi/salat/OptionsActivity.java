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

	private Spinner options, asr, hijri, highLatitudes;
	private Button btnSaveOptions;
	private OptionsDataSource datasource;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        addListenerOnButton();
		addListenerOnSpinnerItemSelection();
		datasource = new CommentsDataSource(this);
		datasource.open();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    public void addListenerOnSpinnerItemSelection() {
		options = (Spinner) findViewById(R.id.options);
		options.setOnItemSelectedListener(this);
		
		asr = (Spinner) findViewById(R.id.asr);
		asr.setOnItemSelectedListener(this);
		
		hijri = (Spinner) findViewById(R.id.hijri);
		hijri.setOnItemSelectedListener(this);
		
		highLatitudes = (Spinner) findViewById(R.id.highLatitudes);
		highLatitudes.setOnItemSelectedListener(this);
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

		options = (Spinner) findViewById(R.id.options);
		asr = (Spinner) findViewById(R.id.asr);		
		hijri = (Spinner) findViewById(R.id.hijri);		
		highLatitudes = (Spinner) findViewById(R.id.highLatitudes);
		
		btnSaveOptions = (Button) findViewById(R.id.saveOptions);

		btnSaveOptions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(OptionsActivity.this,
						"OnClickListener : " + 
						"\nSpinner 1 : " + String.valueOf(options.getSelectedItem()) +
						"\nSpinner 2 : " + String.valueOf(asr.getSelectedItem()) +
						"\nSpinner 3 : " + String.valueOf(hijri.getSelectedItem()) +
						"\nSpinner 4 : " + String.valueOf(highLatitudes.getSelectedItem()),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
