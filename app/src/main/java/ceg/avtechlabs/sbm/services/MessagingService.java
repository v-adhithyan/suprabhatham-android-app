package ceg.avtechlabs.sbm.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by adhithyan-3592 on 30/10/16.
 */
public class MessagingService extends FirebaseMessagingService {
    static final String TAG = "sbm-log";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "from:" + remoteMessage.getFrom());
        Log.d(TAG, "body:" + remoteMessage.getNotification().getBody());
    }
}
