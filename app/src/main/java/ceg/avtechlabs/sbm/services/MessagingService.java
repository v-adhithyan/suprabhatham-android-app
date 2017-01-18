package ceg.avtechlabs.sbm.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import ceg.avtechlabs.sbm.util.Notification;

/**
 * Created by adhithyan-3592 on 30/10/16.
 */
public class MessagingService extends FirebaseMessagingService {
    static final String TAG = "sbm-log";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
      String title = remoteMessage.getNotification().getTitle();
      String message = remoteMessage.getNotification().getBody();

      HashMap<String, Object> arguments = new HashMap<String, Object>();
      arguments.put("title", title);
      arguments.put("message", message);

      Notification.showNotification(this, arguments);
    }
}
