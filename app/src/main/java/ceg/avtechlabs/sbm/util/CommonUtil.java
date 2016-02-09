package ceg.avtechlabs.sbm.util;

import android.content.Context;
import android.content.Intent;

import ceg.avtechlabs.sbm.R;

/**
 * Created by adhithyan-3592 on 07/02/16.
 */

public class CommonUtil {
    static final String SERVER_URL = "http://192.168.1.101/avsbm/register.php";
    static final String SENDER_ID = "suprabhatham-1204";

    static final String TAG = "AVTechLabs-Suprabhatham";

    static final String DISPLAY_MESSAGE = "Push notifications";

    static final String EXTRA_MESSAGE = "message";

    static void displayMessage(Context context, String message){
        Intent intent = new Intent(DISPLAY_MESSAGE);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}
