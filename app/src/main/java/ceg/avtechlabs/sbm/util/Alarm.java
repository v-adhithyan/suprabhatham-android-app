package ceg.avtechlabs.sbm.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import ceg.avtechlabs.sbm.db.DatabaseHandler;

/**
 * Created by adhithyan-3592 on 31/01/16.
 */

public class Alarm {

    public static void setAlarm(Context context, int time){
        Intent intnt = new Intent(context, SongScheduler.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 2011103592, intnt, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pi);
    }

    public static void checkAndSetRecurringAlarm(Context context){
        if(new DatabaseHandler(context).isRecurringAlarm()){
            int day = 24 * 60 * 60 * 1000; //play after 24 hours
            setAlarm(context, day);
        }
    }
}
