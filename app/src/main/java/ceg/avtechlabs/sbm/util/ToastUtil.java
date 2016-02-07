package ceg.avtechlabs.sbm.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by adhithyan-3592 on 07/02/16.
 */
public class ToastUtil {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
