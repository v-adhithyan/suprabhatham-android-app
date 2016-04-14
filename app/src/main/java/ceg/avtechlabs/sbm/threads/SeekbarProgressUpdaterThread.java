package ceg.avtechlabs.sbm.threads;

import android.content.Context;
import android.widget.SeekBar;

import ceg.avtechlabs.sbm.util.audio.PlaybackUtil;

/**
 * Created by adhithyan-3592 on 14/04/16.
 */

public class SeekbarProgressUpdaterThread implements Runnable{

    Context context;
    SeekBar seekBar;
    int totalDuration;
    int elapsedTime = 0;
    boolean playing;

    public SeekbarProgressUpdaterThread(Context context, SeekBar seekBar){
        this.context = context;
        this.seekBar = seekBar;
        playing = true;

    }

    @Override
    public void run() {
        totalDuration = PlaybackUtil.getDurationInSeconds();

        seekBar.setMax(totalDuration);

        for(; elapsedTime <= totalDuration; elapsedTime++){
            timedWaiting();

            try {
                Thread.sleep(1000);
                seekBar.setProgress(elapsedTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void notProgressing(){
        playing = false;
    }

    public void progressingNow(){
        playing = true;
    }

    private void timedWaiting(){
        while(!playing){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
