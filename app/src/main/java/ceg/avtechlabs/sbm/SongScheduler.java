package ceg.avtechlabs.sbm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

public class SongScheduler extends BroadcastReceiver {
    public SongScheduler() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String title = "Suprabhatham";
        String message = "Suprabhatham is playing...";
        context.deleteFile("hr.txt");
        context.deleteFile("min.txt");
        //Toast.makeText(context, "hr and min files deleted..", Toast.LENGTH_LONG).show();
        Vibrator vibrate = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrate.vibrate(2500);
        int id = 3592;
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification nt = new Notification(R.drawable.lord_venkat,title,System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),0);
        nt.setLatestEventInfo(context,title,message,pi);
        nt.flags |=  Notification.FLAG_AUTO_CANCEL;

            nm.notify(id,nt);
        context.startService(new Intent(context, MyService.class));
    }
}
