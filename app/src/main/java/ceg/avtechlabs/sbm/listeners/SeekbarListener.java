package ceg.avtechlabs.sbm.listeners;

import android.widget.SeekBar;

/**
 * Created by adhithyan-3592 on 14/04/16.
 */

public class SeekbarListener{

    SeekBar seekBar;

    public SeekbarListener(SeekBar seekBar){
        this.seekBar = seekBar;

        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
