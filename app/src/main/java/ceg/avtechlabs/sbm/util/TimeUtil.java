package ceg.avtechlabs.sbm.util;

import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by adhithyan-3592 on 06/02/16.
 */
public class TimeUtil {

    public static LinkedList<Integer> getMilliSeconds(int chosenHour, int chosenMinute){
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

        LinkedList<Integer> response = new LinkedList<Integer>();

        response.add(hrs);
        response.add(mins);
        response.add(ms);

        return response;
    }

    public static int getCurrentHour(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MINUTE);
    }

    public static int getMeridian(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.AM_PM);
    }

}
