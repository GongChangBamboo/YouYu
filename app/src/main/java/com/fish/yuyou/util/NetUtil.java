package com.fish.yuyou.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by sun on 2017/4/10 15:56
 */
public class NetUtil {
    public static boolean isNetAvailable=true;//由ReceiveMsgService进行更新网络真实可用性。注意：不用它isNetAvailable来判别是否有网络
    /**
     * 判断是否有网络
     * @param context
     * @return
     */
    public static boolean hasNetWork(Context context)
    {
        ConnectivityManager conManager=
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=conManager.getActiveNetworkInfo();
        if(netInfo!=null&&netInfo.isAvailable())
        {
            return true&&isNetAvailable;
        }
        else {
            return false;
        }
    }

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        NetworkInfo mobileNet=connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mobileNet!=null)
        {
            state = mobileNet.getState();
            if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                return NETWORN_MOBILE;
            }
        }
        return NETWORN_NONE;
    }
}
