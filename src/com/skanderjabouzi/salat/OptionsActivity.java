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

public class OptionsActivity extends Activity implements OnItemSelectedListener{

	private Spinner options, asr, hijri, higherLatitudes;
	private Button btnSubmit;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
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
		options.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		
		asr = (Spinner) findViewById(R.id.asr);
		asr.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		
		hijri = (Spinner) findViewById(R.id.hijri);
		hijri.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		
		higherLatitudes = (Spinner) findViewById(R.id.higherLatitudes);
		higherLatitudes.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

}
