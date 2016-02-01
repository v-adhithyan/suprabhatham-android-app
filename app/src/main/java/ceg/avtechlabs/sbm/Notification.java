package ceg.avtechlabs.sbm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.util.HashMap;

/**
 * Created by adhithyan-3592 on 01/02/16.
 */

public class Notification {

    private static NotificationManager manager;
    private static android.app.Notification notification;

    public static void showNotification(Context context, HashMap<String, Object> arguments){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        String title = arguments.get("title").toString();
        String message = arguments.get("message").toString();
        PendingIntent pi = (PendingIntent) (arguments.containsKey("intent") ? arguments.get("intent") : null);

        int randomNumber = (int)Math.random();

        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.lord_venkat);
        builder.setContentIntent(pi);
        builder.setOngoing(true);
        builder.setNumber(randomNumber);
        builder.build();
        builder.setAutoCancel(true);

        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notification = builder.getNotification();
        manager.notify(2011, notification);

    }
}
