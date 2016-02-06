package ceg.avtechlabs.sbm.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by adhithyan-3592 on 06/02/16.
 */
public class FileUtil {

    public static final String HOUR_FILE = "hr.txt";
    public static final String MINUTE_FILE = "min.txt";

    public static void write(Context context, String fileName, String content){
        try{
            FileOutputStream out = context.openFileOutput(fileName, context.MODE_WORLD_READABLE);

            out.write(content.getBytes());

            out.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static String read(Context context, String fileName){
        String content = "";

        try{
            FileInputStream in = context.openFileInput(fileName);
            
            byte[] buffer = new byte[in.available()];

            in.read(buffer);

            content = new String(buffer);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        return content;
    }
}
