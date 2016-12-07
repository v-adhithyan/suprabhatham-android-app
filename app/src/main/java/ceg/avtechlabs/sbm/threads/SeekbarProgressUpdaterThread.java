package ceg.avtechlabs.sbm.threads;

import android.content.Context;

import ceg.avtechlabs.sbm.util.PlaybackUtil;

import com.triggertrap.seekarc.SeekArc;


/**
 * Created by adhithyan-3592 on 14/04/16.
 */

public class SeekbarProgressUpdaterThread implements Runnable{

    Context context;
    SeekArc seekArc;
    int totalDuration;
    int elapsedTime = 0;
    boolean playing;

    public SeekbarProgressUpdaterThread(Context context, SeekArc seekArc){
        this.context = context;
        this.seekArc = seekArc;
        playing = true;

    }

    @Override
    public void run() {
        totalDuration = PlaybackUtil.getDurationInSeconds();

        //seekArc.setProgressWidth(totalDuration);

        for(; elapsedTime <= totalDuration; elapsedTime++){
            timedWaiting();

            try {
                Thread.sleep(1000);
                seekArc.setProgress(elapsedTime);
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
