package ceg.avtechlabs.sbm.util.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.SeekBar;

import ceg.avtechlabs.sbm.R;
import ceg.avtechlabs.sbm.threads.SeekbarProgressUpdaterThread;

/**
 * Created by adhithyan-3592 on 14/04/16.
 */

public class PlaybackUtil {
    static MediaPlayer player = null;
    static SeekbarProgressUpdaterThread progressUpdaterThread;
    static  SeekBar seekBar;
    static Thread t = null;

    public static void init(Context context, SeekBar seek){

        if(player == null){
            player = MediaPlayer.create(context, R.raw.song);
        }

        seekBar = seek;
        progressUpdaterThread = new SeekbarProgressUpdaterThread(context, seekBar);
    }

    public static void play(Context context){
        //init(context);
        t = new Thread(progressUpdaterThread);
        player.start();
        t.start();
    }

    public static void pause(){
        if(player != null && player.isPlaying()){
            progressUpdaterThread.notProgressing();
            player.pause();
        }
    }

    public static void stop(){
        player.stop();

        if(t.isAlive()){
            t.stop();
        }
    }

    public static void playFromSecond(int sec){
        player.seekTo(sec * 1000);
    }

    public static int getDurationInMillis(){
        return player.getDuration();
    }

    public static int getDurationInSeconds(){
        return getDurationInMillis() / 1000;
    }
}
