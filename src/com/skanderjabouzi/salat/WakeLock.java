package com.skanderjabouzi.salat;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

public class WakeLock {

	private static PowerManager.WakeLock WakeLock;

    static void acquire(Context context) {
        Log.d("SalatWakeLock" , "Acquiring cpu wake lock");
        if (WakeLock != null) {
            return;
        }

        PowerManager pm =
                (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        WakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "Athan Alarm Wake Lock");
        WakeLock.acquire();
    }

    static void release() {
        Log.d("SalatWakeLock" ,"Releasing cpu wake lock");
        if (WakeLock != null) {
            WakeLock.release();
            WakeLock = null;
        }
    }
}
