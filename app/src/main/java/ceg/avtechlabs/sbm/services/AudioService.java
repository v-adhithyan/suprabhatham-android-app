package ceg.avtechlabs.sbm.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.SeekBar;
import android.widget.Toast;

import ceg.avtechlabs.sbm.R;
import ceg.avtechlabs.sbm.threads.SeekbarProgressUpdaterThread;
import ceg.avtechlabs.sbm.tracker.MixPanelUtil;
import ceg.avtechlabs.sbm.util.ToastUtil;
import ceg.avtechlabs.sbm.util.audio.PlaybackUtil;

public class AudioService extends Service implements AudioManager.OnAudioFocusChangeListener {
    MediaPlayer mediaplayer = null;
    WifiManager.WifiLock wifiLock;
    SeekbarProgressUpdaterThread progressUpdaterThread;
    SeekBar seekBar;
    Thread t;

    public AudioService(){

    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange)
        {
            case AudioManager.AUDIOFOCUS_GAIN:
                if(mediaplayer == null)
                {
                    mediaplayer = MediaPlayer.create(this, R.raw.song);
                    mediaplayer.setWakeMode(this,PowerManager.PARTIAL_WAKE_LOCK);
                    mediaplayer.start();
                }
                 if(!mediaplayer.isPlaying())
                    mediaplayer.start();
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                mediaplayer.stop();
                super.onDestroy();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                if(mediaplayer.isPlaying())
                    mediaplayer.pause();

                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if(mediaplayer.isPlaying())
                    mediaplayer.pause();
                break;
        }
    }

    public int onStartCommand(Intent intent,int flags,int startId)
    {

        //if(extra.equals("play")) {


            ToastUtil.showToast(getApplicationContext(), "Suprabhatham is playing in background.");
            wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "SuprabhathamLock");
            wifiLock.acquire();
            mediaplayer = MediaPlayer.create(this, R.raw.song);
            mediaplayer.start();

            MixPanelUtil.getInstance(getApplicationContext()).trackEvent("ALARM");
        /*}

        else if(extra.equals("pause"))
        {
            if(mediaplayer.isPlaying())
                mediaplayer.pause();
        }*/

        return START_STICKY;
    }
    public void onDestroy()
    {
        super.onDestroy();
        mediaplayer.stop();
        mediaplayer.reset();
        wifiLock.release();
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
        ToastUtil.showToast(getApplicationContext(), "Suprabhatham playback is stopped.");
    }

    public void pausePlayBack()
    {
        mediaplayer.pause();
    }
}
