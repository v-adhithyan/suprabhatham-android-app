package ceg.avtechlabs.sbm.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by adhithyan-3592 on 07/02/16.
 */

public class AlertManager {

    public static void showAlertDialog(Context context, String title, String message){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)

        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
