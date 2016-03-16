package ceg.avtechlabs.sbm.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by adhithyan-3592 on 16/03/16.
 */
public class DisplayUtil {

    private static Point getSize(Context context){
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    public static int getScreenWidth(Context context){
        return getSize(context).x;
    }

    public static int getScreenHeight(Context context){
        return getSize(context).y;
    }
}
