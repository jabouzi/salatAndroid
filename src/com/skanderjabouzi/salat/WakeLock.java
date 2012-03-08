package com.skanderjabouzi.salat;

import android.content.Context;
import android.os.PowerManager;

public class WakeLock {

	private static PowerManager.WakeLock wakeLock = null;

	public static void acquire(Context context) {
		if(wakeLock != null && wakeLock.isHeld()) return;

		wakeLock = ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK, "Athan Alarm Wake Lock");
		wakeLock.acquire();
	}

	public static void release() {
		if(wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
		}
		wakeLock = null;
	}
}
