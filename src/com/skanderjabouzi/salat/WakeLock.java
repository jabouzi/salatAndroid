package com.skanderjabouzi.salat;

import android.content.Context;
import android.os.PowerManager;
import android.app.KeyguardManager;
import android.util.Log;

public class WakeLock {

	private static PowerManager.WakeLock WakeLock;
	private static KeyguardManager km;
	private static KeyguardManager.KeyguardLock kl;

    static void acquire(Context context) {
        Log.i("SalatWakeLock" , "Acquiring cpu wake lock");
        if (WakeLock != null) {
            return;
        }

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        WakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Athan Alarm Wake Lock");
        WakeLock.acquire();
    }
    
    static void unlock(Context context)
    {
		Log.i("SalatWakeLock" ,"Disabling Keyguard");
		km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("SALAT");
		kl.disableKeyguard();
	}
	
    static void release() {
        Log.i("SalatWakeLock" ,"Releasing cpu wake lock");
        if (WakeLock != null) {
            WakeLock.release();
            WakeLock = null;
        }
    }
    
    static void lock()
	{
		Log.i("SalatWakeLock" ,"Reenabling Keyguard");
		kl.reenableKeyguard();
	}
}
