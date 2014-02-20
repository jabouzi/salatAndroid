package com.skanderjabouzi.salat;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
//Automatically called when State Change is Detected because this Receiver is Registered for PHONE_STATE intent filter in AndroidManifest.xml
public class SalatPhoneReceiver extends BroadcastReceiver {
	
	TelephonyManager manager;       
	PhoneStateMonitor phoneStateListener;
	static boolean isAlreadyListening=false;

	//This Method automatically Executed when Phone State Change is Detected
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		phoneStateListener =new PhoneStateMonitor(context);//Creating the Object of Listener
		manager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);//Getting the Telephony Service Object
		if(!isAlreadyListening)//Checking Listener is Not Registered with Telephony Services
		{
            manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);//Registering the Listener with Telephony to listen the State Change
			isAlreadyListening=true;  //setting true to indicate that Listener is listening the Phone State
		}

	}

}

/*import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class SalatPhoneReceiver extends BroadcastReceiver {
	
	public void onReceive(Context context, Intent intent) {
        
	   // TELEPHONY MANAGER class object to register one listner
		TelephonyManager tmgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
				
		//Create Listner
		MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
		
		// Register listener for LISTEN_CALL_STATE
		tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
        
            Log.d("MyPhoneListener",state+"   incoming no:"+incomingNumber);

            if (state == 1) {

                String msg = "New Phone Call Event. Incomming Number : "+incomingNumber;
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(context, msg, duration).show();
            }
        }
    }
}
*/
