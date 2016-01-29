package com.avtechlabs.push.notifications;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by adhithyan-3592 on 29/01/16.
 */

public class ConnectionDetector {

    private Context context;

    public ConnectionDetector(Context context){
        this.context = context;
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivity != null){
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if(info != null){
                for(int i=0; i< info.length; i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
