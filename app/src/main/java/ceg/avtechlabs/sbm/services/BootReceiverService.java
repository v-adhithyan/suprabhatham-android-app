package ceg.avtechlabs.sbm.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ceg.avtechlabs.sbm.util.Alarm;
import ceg.avtechlabs.sbm.util.FileUtil;
import ceg.avtechlabs.sbm.util.TimeUtil;

public class BootReceiverService extends BroadcastReceiver {
    public BootReceiverService() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //Toast.makeText(context,"Boot completed",Toast.LENGTH_LONG).show();

        int chosenHour = 25, chosenMinute = 61;
        boolean noSchedule = false;


        String hour = FileUtil.read(context, FileUtil.HOUR_FILE);
        String minute = FileUtil.read(context, FileUtil.MINUTE_FILE);

        if((hour.equals("") && minute.equals("")) || (hour == null && minute == null)){
            noSchedule = true;
        }
        else{
            chosenHour = Integer.parseInt(hour);
            chosenMinute = Integer.parseInt(minute);
        }

        if(noSchedule)
        ;
        else if(chosenHour!=25 &&chosenMinute!=61)
        {
            int ms = TimeUtil.getMilliSeconds(chosenHour, chosenMinute).get(2);
            Alarm.setAlarm(context, ms);

        }
    }
}
