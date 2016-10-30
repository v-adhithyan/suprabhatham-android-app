package ceg.avtechlabs.sbm.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by adhithyan-3592 on 30/10/16.
 */

public class InstanceIdService extends FirebaseInstanceIdService {
    static final String TAG = "sbm-log";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "TOKEN:" + token);
    }
}
