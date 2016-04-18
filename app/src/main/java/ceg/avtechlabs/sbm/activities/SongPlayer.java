package ceg.avtechlabs.sbm.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.triggertrap.seekarc.SeekArc;

import ceg.avtechlabs.sbm.Main_Activity;
import ceg.avtechlabs.sbm.R;
import ceg.avtechlabs.sbm.util.ToastUtil;
import ceg.avtechlabs.sbm.util.audio.PlaybackUtil;

public class SongPlayer extends AppCompatActivity {

    FloatingActionButton playBackButton;
    SeekArc seekArc;
    MediaPlayer player;
    Thread t;
    ProgressUpdater progressUpdater;
    int duration = 0;
    boolean playing = false, paused = false;
    TextView textViewDuration;
    int min = 0, sec = 0;
    String preMin = "", preSec = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBarColor)));
        getSupportActionBar().hide();

        setTitle("");

        playBackButton = (FloatingActionButton)findViewById(R.id.playBackButton);
        seekArc = (SeekArc)findViewById(R.id.seekArc);
        textViewDuration = (TextView)findViewById(R.id.textViewDuration);
        textViewDuration.setText("00:00");

        PlaybackUtil.init(this, seekArc);
        duration = PlaybackUtil.getDurationInSeconds();

        seekArc.setRoundedEdges(true);
        player = PlaybackUtil.getPlayer();

        AdView mAdView = (AdView) findViewById(R.id.adViewSongPlayer);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void exit(View v){
        Intent intent = new Intent(SongPlayer.this, Main_Activity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){

        if(player != null){
            if(player.isPlaying()){
                player.stop();
            }
        }

        finish();
    }

    public void controlPlayback(View v){
            if(player.isPlaying()){
                player.pause();
                playBackButton.setImageResource(android.R.drawable.ic_media_play);

                playing = false;
            }else{

                if(t == null){
                    progressUpdater = new ProgressUpdater();
                    t = new Thread(progressUpdater);
                    t.start();
                }

                player.start();


                playBackButton.setImageResource(android.R.drawable.ic_media_pause);

                playing = true;
            }
    }

    public void updateView(final int i){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekArc.setProgress(i);
                updateDuration(i);

                if(i == 1232){

                }
            }
        });
    }

    class ProgressUpdater implements Runnable{

        @Override
        public void run() {
            for(int i=0; i<=duration; i++){

                while(!playing){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                updateView(i);
            }
        }
    }

    public void updateDuration(int i){
        sec = i % 60;

        if(sec == 0){
            min = i / 60;
        }

        preSec = (sec < 10) ? "0" : "";
        preMin = (min < 10) ? "0" : "";

        textViewDuration.setText(preMin + min + ":" +  preSec + sec);
    }
}
