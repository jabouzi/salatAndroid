package com.skanderjabouzi.salat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneStateMonitor extends PhoneStateListener {
	Context context;

	public PhoneStateMonitor(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context=context;
	}

	//This Method Automatically called when changes is detected in Phone State
	public void onCallStateChanged(int state, String incomingNumber) {
		// TODO Auto-generated method stub
		super.onCallStateChanged(state, incomingNumber);
		
		Toast.makeText(context, "Phone State - "+state+" Incoming Number - "+incomingNumber, Toast.LENGTH_LONG).show();//Giving the Message that Phone State Changed
		//Checking The phone state  
		switch(state)
		{
		case TelephonyManager.CALL_STATE_IDLE:    //Phone is in Idle State
			Toast.makeText(context, "Phone State is IDLE", Toast.LENGTH_LONG).show();
			break;
		case TelephonyManager.CALL_STATE_RINGING:  //Phone is Ringing
			Toast.makeText(context, "Phone State is RINGING", Toast.LENGTH_LONG).show();
			//stopService(new Intent(this, AthanService.class));
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:  //Call is Received
			Toast.makeText(context, "Call State is OFFHOOK",Toast.LENGTH_LONG).show();
			break;
		}
	}
}
