package ceg.avtechlabs.sbm.util;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by adhithyan-3592 on 07/02/16.
 */

public class ServerUtil {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;

    private static final Random random = new Random();

    public static void register(final Context context, String name, String email, String regId){
        Log.d(CommonUtil.TAG, "device id:" + regId);
        ToastUtil.showToast(context, "hello"+regId);
        String SERVER_URL = CommonUtil.SERVER_URL;

        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", regId);
        params.put("name", name);
        params.put("email", email);

        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);

        for(int i=1; i<=MAX_ATTEMPTS; i++){
            post(SERVER_URL, params);
            GCMRegistrar.setRegisteredOnServer(context, true);
            ToastUtil.showToast(context, "Device registered for push notifications");
        }
    }

    public static void unregister(Context context, String id){
        ToastUtil.showToast(context, "Device unregistered..");
    }

    public static void post(String endPoint, Map<String, String> params){
        URL url = null;

        try{
            url = new URL(endPoint);
        }catch(MalformedURLException e){
            Log.e("post url", "invalid url");
        }

        StringBuilder sb = new StringBuilder();

        Iterator<String> it = params.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            String value = (String)params.get(key);
            sb.append(key);
            sb.append("=");
            sb.append(value);

            if(it.hasNext()) {
                sb.append("&");
            }
        }
        String body = sb.toString();
        Log.v("body", body);

        byte[] bytes = body.getBytes();

        HttpURLConnection connection = null;
        try{
            connection = (HttpURLConnection)url.openConnection();

            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setFixedLengthStreamingMode(bytes.length);
            connection.setRequestMethod("POST");

            OutputStream out = connection.getOutputStream();
            out.write(bytes);
            out.close();

            int status = connection.getResponseCode();

            if(status == 200){
                Log.d("status", "success");
            }else{
                Log.d("status", status + "");
            }
        }catch(Exception ex){
            Log.e("connection", ex.toString());
        }finally {
            connection.disconnect();

        }

    }
}
