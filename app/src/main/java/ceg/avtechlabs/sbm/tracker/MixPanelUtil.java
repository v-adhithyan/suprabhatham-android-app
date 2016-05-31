package ceg.avtechlabs.sbm.tracker;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import ceg.avtechlabs.sbm.util.InfoUtil;

/**
 * Created by adhithyan-3592 on 31/05/16.
 */

public class MixPanelUtil {
    public static String PROJECT_TOKEN = "be8ff697450058991ca9f08b1de4d4e8";
    public static MixPanelUtil mixPanelUtil = null;
    public Context context;
    public MixpanelAPI mixpanelAPI;

    private MixPanelUtil(Context context){
        this.context = context;
        mixpanelAPI = MixpanelAPI.getInstance(context, PROJECT_TOKEN);
    }

    public static MixPanelUtil getInstance(Context context){
        if(mixPanelUtil == null){
            mixPanelUtil = new MixPanelUtil(context);
        }

        return mixPanelUtil;
    }

    public void trackEvent(String eventName){
        JSONObject props = new JSONObject();

        try {
            props.put("Email", InfoUtil.getEmailAddress(context));
            props.put("Date", new Date().toString());
            mixpanelAPI.track(eventName, props);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
    }
}
