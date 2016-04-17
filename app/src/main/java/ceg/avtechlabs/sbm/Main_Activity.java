package ceg.avtechlabs.sbm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aavilabs.db.DatabaseHandler;
import com.buddy.sdk.Buddy;
import com.buddy.sdk.BuddyCallback;
import com.buddy.sdk.BuddyResult;
import com.buddy.sdk.models.User;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.JsonObject;

import java.sql.Time;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ceg.avtechlabs.sbm.activities.SongPlayer;
import ceg.avtechlabs.sbm.util.AlertManager;
import ceg.avtechlabs.sbm.util.FileUtil;
import ceg.avtechlabs.sbm.util.InfoUtil;
import ceg.avtechlabs.sbm.util.TimeUtil;
import ceg.avtechlabs.sbm.util.ToastUtil;
import ceg.avtechlabs.sbm.util.audio.PlaybackUtil;


public class Main_Activity extends ActionBarActivity {
    TimePicker tp;
    final int TIME_DIALOG_ID=999;
    int chosenHour = 25,chosenMinute = 61 ,hour,min ,amORpm;
    MediaPlayer player;
    boolean songPlaying, timeChosen = false;
    Spinner spinner;
    boolean cancel = false;
    Runnable timeListener = null;

    SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBarColor)));
        setTitle(R.string.app_name);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String email = InfoUtil.getEmailAddress(this);
        String pass = InfoUtil.getDeviceName(this);

        Buddy.init(getApplicationContext(), "bbbbbc.jwrpzrKlGHdfc", "091cffe2-5d16-3279-932b-546c96b45b93");

        Buddy.createUser(email, pass, null, null, null, null, null, null, null);

        Buddy.loginUser(email, pass, null);
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
                    .setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent rateIntent = new Intent(Intent.ACTION_VIEW);
                            rateIntent.setData(Uri.parse("market://details?id=ceg.avtechlabs.sbm"));
                            if (!rateMyApplication(rateIntent)) {
                                ToastUtil.showToast(getApplicationContext(), "Could not open play store.Try after some time");
                            }
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
        /*MyService service = new MyService(seekBar);
        startService(new Intent(getBaseContext(), service.getClass()));*/

        Intent intent = new Intent(Main_Activity.this, SongPlayer.class);
        startActivity(intent);
    }
    /*public void pauseSong(View v)
    {
        Intent intent = new Intent(getBaseContext(),MyService.class);
        intent.putExtra("EXTRA","pause");
        startService(intent);
    }*/
    public void stopSong(View v)
    {
        stopService(new Intent(getBaseContext(), MyService.class));
    }
    public void startScheduler(View v)
    {
        initTime();
        showDialog(TIME_DIALOG_ID);

    }



    public void schedule()
    {

        String ap = "";
        int hr;

        hr = (chosenHour==0)?12:chosenHour;
        amORpm = (chosenHour>=0&&chosenHour<12)?1:2;
        if(amORpm==1)
            ap = "AM";
        else
            ap = "PM";

        //this condition checks if hour and minute are equal to initialized values. If so, the user has not chosen time, so don't display the alert.
        //ToastUtil.showToast(getApplicationContext(), "inside chooset ime algo");
        if(chosenHour != 25 && chosenMinute != 61){
            //ToastUtil.showToast(getApplicationContext(), "in if");
            AlertDialog.Builder scheduleAlerter = new AlertDialog.Builder(this).setTitle("Scheduler")
                    .setMessage("Schedule Suprabhatham to play at " + hr + ":" + chosenMinute + " " + ap)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FileUtil.write(getApplicationContext(), FileUtil.HOUR_FILE, chosenHour + "");
                            FileUtil.write(getApplicationContext(), FileUtil.MINUTE_FILE, chosenMinute + "");

                            LinkedList<Integer> time = TimeUtil.getMilliSeconds(chosenHour, chosenMinute);

                            int hrs = time.get(0);
                            int mins = time.get(1);
                            int ms = time.get(2);

                            recurringAlert();

                            Alarm.setAlarm(getApplicationContext(), ms);
                            ToastUtil.showToast(getApplicationContext(), "Suprabhadham will play after " + hrs + " hours " + mins + " minutes");
                        }

                    })
                    .setNegativeButton("No", null);
            scheduleAlerter.show();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
            switch (id)
            {
                case TIME_DIALOG_ID:

                    final TimePickerDialog timePickerDialog =  new TimePickerDialog(this, tpl, TimeUtil.getCurrentHour() ,TimeUtil.getCurrentMinute(),false);
                    timePickerDialog.setCancelable(true);
                    timePickerDialog.setCanceledOnTouchOutside(true);
                    timeChosen = false;


                        if(isOsLowerThanLollipop()){
                            timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "Schedule", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //ToastUtil.showToast(getApplicationContext(), "time chosen");
                                    timeChosen = true;

                                }
                            });
                            timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    timeChosen = false;
                                }
                            });
                        }

                    return timePickerDialog;

            }


        return null;
    }


    public void initTime()
    {
        tp = (TimePicker)findViewById(R.id.timePicker);

    }

    final TimePickerDialog.OnTimeSetListener tpl = new TimePickerDialog.OnTimeSetListener(){
        public void onTimeSet(TimePicker view, final int shour, final int smin) {
            //Toast.makeText(getApplicationContext(), shour + ":" + smin, Toast.LENGTH_SHORT).show();
            chosenHour = shour;
            chosenMinute = smin;

            if(!timeChosen && isOsLowerThanLollipop()){
                return;
            }else{
                schedule();
            }


        }
    };

    @Override
    public void onDestroy()
    {
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(3592);

        Buddy.logoutUser(null);

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

    private boolean isOsLowerThanLollipop(){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}
