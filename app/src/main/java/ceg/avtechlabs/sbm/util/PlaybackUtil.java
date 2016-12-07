package ceg.avtechlabs.sbm.util;

import android.content.Context;
import android.media.MediaPlayer;

import com.triggertrap.seekarc.SeekArc;

import ceg.avtechlabs.sbm.R;
import ceg.avtechlabs.sbm.threads.SeekbarProgressUpdaterThread;

/**
 * Created by adhithyan-3592 on 14/04/16.
 */

public class PlaybackUtil {
    static MediaPlayer player = null;
    static SeekbarProgressUpdaterThread progressUpdaterThread;
    static SeekArc seekArc;
    static Thread t = null;

    public static void init(Context context, SeekArc seek){

        if(player == null){
            player = MediaPlayer.create(context, R.raw.song);
        }

        seekArc = seek;
        progressUpdaterThread = new SeekbarProgressUpdaterThread(context, seekArc);
    }

    public static MediaPlayer getPlayer(){
        return player;
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

    public static boolean isPlaying(){
        return player.isPlaying();
    }
}
