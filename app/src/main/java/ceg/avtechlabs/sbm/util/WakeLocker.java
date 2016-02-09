package ceg.avtechlabs.sbm.util;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by adhithyan-3592 on 07/02/16.
 */

public class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context context){

        if(wakeLock != null){
            wakeLock.release();
        }

        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);

        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "WakeLock");
        wakeLock.acquire();
    }

    public static void release(){
        if(wakeLock != null){
            wakeLock.release();
            wakeLock = null;
        }
    }
}
