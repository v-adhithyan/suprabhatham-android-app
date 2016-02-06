package ceg.avtechlabs.sbm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aavilabs.db.DatabaseHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.LinkedList;

import ceg.avtechlabs.sbm.util.FileUtil;
import ceg.avtechlabs.sbm.util.TimeUtil;


public class Main_Activity extends ActionBarActivity {
    TimePicker tp;
    final int TIME_DIALOG_ID=999;
    int chosenHour = 25,chosenMinute = 61 ,hour,min ,amORpm;
    MediaPlayer player;
    boolean songPlaying;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBarColor)));
        setTitle(R.string.app_name);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Rate and Review")
                    .setMessage("Do you like the application ? Please spare a moment to rate and review the app.")
                    .setPositiveButton("Rate Now", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent rateIntent = new Intent(Intent.ACTION_VIEW);
                            rateIntent.setData(Uri.parse("market://details?id=ceg.avtechlabs.sbm"));
                            if(!rateMyApplication(rateIntent))
                                Toast.makeText(getApplicationContext(),"Could not open play store.Try after some time",Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setNegativeButton("Rate Later", null);
            alertBuilder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
       AlertDialog.Builder exitAlert =  new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit confirmation")
                .setMessage("Do you want to exit the application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null);

        exitAlert.show();

    }

    public void playSong(View v)
    {
        int id = 3592;
        startService(new Intent(getBaseContext(),MyService.class));
    }
    /*public void pauseSong(View v)
    {
        Intent intent = new Intent(getBaseContext(),MyService.class);
        intent.putExtra("EXTRA","pause");
        startService(intent);
    }*/
    public void stopSong(View v)
    {
        stopService(new Intent(getBaseContext(),MyService.class));
    }
    public void startScheduler(View v)
    {
        initTime();
        showDialog(TIME_DIALOG_ID);

    }



    public void chooseTime()
    {

        String ap = "";
        int hr;
        hr = (chosenHour>12)?(chosenHour-12):chosenHour;
        hr = (chosenHour==0)?12:chosenHour;
        amORpm = (chosenHour>=0&&chosenHour<12)?1:2;
        if(amORpm==1)
            ap = "AM";
        else
            ap = "PM";
        AlertDialog.Builder scheduleAlerter = new AlertDialog.Builder(this).setTitle("Scheduler")
                .setMessage("Schedule Suprabhatham to play at " + hr + ":" + chosenMinute + " " + ap)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar cal = Calendar.getInstance();
                        int chour = cal.get(Calendar.HOUR_OF_DAY);
                        int cmin = cal.get(Calendar.MINUTE);
                        int hrs = 0, mins = 0;
                        boolean negativeHr = false;
                        String tmphr = chosenHour + "" ;
                        String tmpmin = chosenMinute +"";

                        FileUtil.write(getApplicationContext(), FileUtil.HOUR_FILE, tmphr);
                        FileUtil.write(getApplicationContext(), FileUtil.MINUTE_FILE, tmpmin);

                        LinkedList<Integer> time = TimeUtil.getMilliSeconds(chosenHour, chosenMinute);

                        hrs = time.get(0);
                        mins = time.get(1);
                        int ms = time.get(2);

                        recurringAlert();

                        Intent intent = new Intent(getApplicationContext(), SongScheduler.class);
                        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 2011103592, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ms, pi);
                        Toast.makeText(getApplicationContext(), "Suprabhadham will play after " + hrs + " hours " + mins + " minutes from now", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("No", null);
        scheduleAlerter.show();
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,tpl,hour,min,false);

        }
        return null;
    }
    public void initTime()
    {
        tp = (TimePicker)findViewById(R.id.timePicker);
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        tp.setCurrentHour(hour);
        tp.setCurrentMinute(min);
    }

    private TimePickerDialog.OnTimeSetListener tpl = new TimePickerDialog.OnTimeSetListener(){
        public void onTimeSet(TimePicker view,int shour,int smin)
        {
            //Toast.makeText(getApplicationContext(), shour + ":" + smin, Toast.LENGTH_SHORT).show();
            chosenHour = shour;
            chosenMinute = smin;
            chooseTime();
        }

    };
    @Override
    public void onDestroy()
    {
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(3592);
        super.onDestroy();
    }

    public void viewLyrics(View v)
    {
        Intent intent = new Intent(getApplicationContext(),Lyrics.class);
        startActivity(intent);
    }


    private boolean rateMyApplication(Intent intent)
    {
        try
        {
            startActivity(intent);
            return true;
        }
        catch(ActivityNotFoundException ex)
        {
            return false;
        }

    }

    private void recurringAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle("Confirmation")
                .setMessage("Do you want to play Suprabhatham at scheduled time everyday?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        db.insert(DatabaseHandler.RECURRING_TABLE, System.currentTimeMillis() + "");
                    }
                })
                .setNegativeButton("No", null);
        alert.show();
    }
}
