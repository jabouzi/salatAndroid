package com.skanderjabouzi.salat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

class MidnightReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		setSalatTimes();
		String salatName = intent.getStringExtra("SALATTIME");
		Toast.makeText(context, "It's Salat " + salatName + "time ", Toast.LENGTH_LONG).show();
		//String msg_for_me = intent.getStringExtra("NEW_STATUS_EXTRA_COUNT");
		Log.d("SalatReceiver", salatName);
	}
}
