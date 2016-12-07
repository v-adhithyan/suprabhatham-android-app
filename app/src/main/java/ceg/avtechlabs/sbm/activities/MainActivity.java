package ceg.avtechlabs.sbm.activities;

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
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;

import ceg.avtechlabs.sbm.db.DatabaseHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.LinkedList;

import ceg.avtechlabs.sbm.util.Alarm;
import ceg.avtechlabs.sbm.services.AudioService;
import ceg.avtechlabs.sbm.R;
import ceg.avtechlabs.sbm.tracker.MixPanelUtil;
import ceg.avtechlabs.sbm.util.FileUtil;
import ceg.avtechlabs.sbm.util.InfoUtil;
import ceg.avtechlabs.sbm.util.TimeUtil;
import ceg.avtechlabs.sbm.util.ToastUtil;


public class MainActivity extends ActionBarActivity {
    TimePicker tp;
    final int TIME_DIALOG_ID = 999;
    int chosenHour = 25,chosenMinute = 61 ,hour,min ,amORpm;
    MediaPlayer player;
    boolean songPlaying, timeChosen = false;
    Spinner spinner;
    boolean cancel = false;
    Runnable timeListener = null;

    SeekBar seekBar;

    MixPanelUtil mixPanelUtil;
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

        mixPanelUtil = MixPanelUtil.getInstance(this);
        mixPanelUtil.trackEvent("Main Activity");
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

        String title = getString(R.string.rate_app_title);
        String message = getString(R.string.rate_app_message);
        String ok = getString(R.string.rate_app_ok);
        String cancel = getString(R.string.rate_app_cancel);
        final String uri = getString(R.string.rate_app_uri);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent rateIntent = new Intent(Intent.ACTION_VIEW);
                            rateIntent.setData(Uri.parse(uri));
                            if (!rateMyApplication(rateIntent)) {
                                ToastUtil.showToast(getApplicationContext(), getString(R.string
                                    .rate_app_error_playstore));
                            }
                        }

                    })
                    .setNegativeButton(cancel, null);
            alertBuilder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
        String title = getString(R.string.exit_conf_title);
        String message = getString(R.string.exit_conf_message);
        String yes = getString(R.string.yes);
        String no = getString(R.string.no);

        AlertDialog.Builder exitAlert =  new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton(no, null);

        exitAlert.show();

    }

    public void playSong(View v)
    {
        mixPanelUtil.trackEvent("Play Song");
        int id = 3592;
        /*MyService service = new MyService(seekBar);
        startService(new Intent(getBaseContext(), service.getClass()));*/

        Intent intent = new Intent(MainActivity.this, SongPlayerActivity.class);
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
        stopService(new Intent(getBaseContext(), AudioService.class));
    }
    public void startScheduler(View v)
    {
        initTime();
        showDialog(TIME_DIALOG_ID);
        mixPanelUtil.trackEvent("Scheduler");
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

        String yes = getString(R.string.yes);
        String no = getString(R.string.no);
        String title = getString(R.string.scheduler_title);
        String message = String.format(getString(R.string.scheduler_message), hr, chosenMinute,
            ap);


        //this condition checks if hour and minute are equal to initialized values. If so, the user has not chosen time, so don't display the alert.
        //ToastUtil.showToast(getApplicationContext(), "inside chooset ime algo");
        if(chosenHour != 25 && chosenMinute != 61){
            //ToastUtil.showToast(getApplicationContext(), "in if");
            AlertDialog.Builder scheduleAlerter = new AlertDialog.Builder(this).setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(yes, new DialogInterface.OnClickListener() {
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

                            String toastMessage = String.format(getString(R.string.scheduler_toast)
                              , hrs, mins);
                            ToastUtil.showToast(getApplicationContext(), toastMessage);

                            mixPanelUtil.trackEvent("Scheduler Ok");
                        }

                    })
                    .setNegativeButton(no, null);
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
                            timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE,
                                getString(R.string.schedule),
                                new DialogInterface.OnClickListener() {
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

        //Buddy.logoutUser(null);

        super.onDestroy();
    }

    public void viewLyrics(View v)
    {

        mixPanelUtil.trackEvent("View Lyrics");
        Intent intent = new Intent(getApplicationContext(), LyricsActivity.class);
        startActivity(intent);


    }


    private boolean rateMyApplication(Intent intent)
    {
        try
        {
            mixPanelUtil.trackEvent("Rate App");
            startActivity(intent);
            return true;
        }
        catch(ActivityNotFoundException ex)
        {
            return false;
        }

    }

    private void recurringAlert(){

        String yes = getString(R.string.yes);
        String no = getString(R.string.no);
        String title = getString(R.string.recur_alert_title);
        String message = getString(R.string.recur_alert_message);

        AlertDialog.Builder alert = new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        db.insert(DatabaseHandler.RECURRING_TABLE, System.currentTimeMillis() + "");

                        mixPanelUtil.trackEvent("Recurring Alert:Yes");
                    }
                })
                .setNegativeButton(no, null);
        alert.show();
    }

    private boolean isOsLowerThanLollipop(){
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}
