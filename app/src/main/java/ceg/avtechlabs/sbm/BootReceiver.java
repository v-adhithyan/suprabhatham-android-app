package ceg.avtechlabs.sbm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //Toast.makeText(context,"Boot completed",Toast.LENGTH_LONG).show();

        int chosenHour = 25, chosenMinute = 61;
        boolean noSchedule = false;
        try{
            FileInputStream fin1 = context.openFileInput("hr.txt");
            FileInputStream fin2 = context.openFileInput("min.txt");
            //Toast.makeText(context,"fin1:"+fin1+" fin2:"+fin2,Toast.LENGTH_LONG).show();
            if(fin1==null && fin2 ==null)
            {
                noSchedule = true;
                //Toast.makeText(context,"No date and time files.",Toast.LENGTH_LONG).show();
            }
            else
            {
                byte[] buffer = new byte[fin1.available()];
                fin1.read(buffer);
                String time = new String(buffer);
                chosenHour = Integer.parseInt(time);
                buffer = new byte[fin2.available()];
                fin2.read(buffer);
                time = new String(buffer);
                chosenMinute = Integer.parseInt(time);
                //Toast.makeText(context,"Boot receiver.,"+chosenHour+":"+chosenMinute,Toast.LENGTH_LONG).show();
                fin1.close();
                fin2.close();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        if(noSchedule)
        {}//Toast.makeText(context,"No schedule.",Toast.LENGTH_LONG).show();
        else if(chosenHour!=25 &&chosenMinute!=61)
        {
            Calendar cal = Calendar.getInstance();
            int chour = cal.get(Calendar.HOUR_OF_DAY);
            int cmin = cal.get(Calendar.MINUTE);
            int hrs = 0, mins = 0;
            boolean negativeHr = false;

            if (chosenHour < chour) {

                hrs = -24 + (chour - chosenHour);

            }
            if (chosenHour == chour) {
                if (chosenMinute < cmin)
                    hrs = 24;
                else
                    hrs = 0;
            } else
                hrs = chosenHour - chour;
            if (hrs < 0) {
                negativeHr = true;
                hrs = 24 + hrs;
            }
            //Toast.makeText(getApplicationContext(), "Expected hour:"+hrs, Toast.LENGTH_SHORT).show();

            if (chosenMinute < cmin) {

                if (hrs != 0)
                    hrs--;

                mins = 60 - (cmin - chosenMinute);
                // Toast.makeText(getApplicationContext(), "Expected hour:"+hrs, Toast.LENGTH_SHORT).show();

            } else {
                mins = chosenMinute - cmin;
            }
            //Toast.makeText(getApplicationContext(), "Expected min:"+mins, Toast.LENGTH_LONG).show();

            int ms = (hrs * 60 * 60 * 1000) + (mins * 60 * 1000);

            Intent intnt = new Intent(context, SongScheduler.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, 2011103592, intnt, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ms, pi);
            Toast.makeText(context, "Suprabhadham will play after " + hrs + " hours " + mins + " minutes from now", Toast.LENGTH_SHORT).show();


        }
    }
}
