package com.fish.yuyou.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Rect;
import android.location.LocationManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.fish.yuyou.base.YuApplication;

import java.util.List;
import java.util.Locale;

/**
 * Created by lifukun on 2017/11/22.
 */

public class CommonUtil {
    private static final int MIN_DELAY_TIME = 1500;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    //判断是否快速点击
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * 获取栈顶Activity
     *
     * @return
     */
    public static String getTopActivity(Context context) {
        //_context是一个保存的上下文
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName();
    }

    //所有的showTost
    public static void show(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isChinese() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = YuApplication.getInstance().getResources().getConfiguration().getLocales().get(0);

        } else {
            locale = YuApplication.getInstance().getResources().getConfiguration().locale;
        }
        String lang = locale.getLanguage() + "-" + locale.getCountry();

        return locale.getLanguage().toLowerCase().startsWith("zh");
    }
    /**
     * 获取服务器时间
     */

    /**
     * 获取当前时间（服务器的时间）（单位:秒）
     *
     * @return
     */
    public static long getCurrentTimeAsSecond() {
        //  return (System.currentTimeMillis() + CzdlApplication.getInstance().getDiffLong()) / 1000;
        return (System.currentTimeMillis()) / 1000;
    }

    //获取当前时间
    public static Long getCurrentMinute() {
        long millis = System.currentTimeMillis();

        return millis / 1000;
    }

    public static int getStayMinute(long t) {
        long curr = System.currentTimeMillis() / 1000;
        long stay = curr - t;

        if (stay > 0) {
            return (int) stay;
        }
        return 1;
    }

    public static void closeKeyboard(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 检测网络状态
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        return NetUtil.hasNetWork(context);
    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isGPSOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    /**
     * 判断服务是否处于运行状态.
     *
     * @param servicename
     * @param context
     * @return
     */
    public static boolean isServiceRunning(String servicename, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : infos) {
            if (servicename.equals(info.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //判断是否在栈中MainActivity是否存在
    public static boolean isInStack(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(5);
        if (RunningTask != null && RunningTask.size() > 0) {
            for (int i = 0; i < RunningTask.size(); i++) {
                ActivityManager.RunningTaskInfo taskInfo = RunningTask.get(i);
                if ("com.android.cnki.czdlmobile.activity.MainActivity".equals(taskInfo.baseActivity.getClassName())) {
                    return true;
                }

            }

        }
        return false;
    }

    /**
     * 获取屏幕宽度dip
     */
    public static int getAndroiodScreenProperty(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
//        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
//        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        return screenWidth;
    }

    /**
     * 判断view是否被其他view遮挡，即判断一个View是否在屏幕中完全显示，能完全被看到
     * Created by zhangxinyu on 2018/10/31.
     *
     * @param view 待判断的view
     */
    public static boolean judgeViewCovered(final View view) {
        View currentView = view;

        Rect currentViewRect = new Rect();
        boolean partVisible = currentView.getGlobalVisibleRect(currentViewRect);
        boolean totalHeightVisible = (currentViewRect.bottom - currentViewRect.top) >= view.getMeasuredHeight();
        boolean totalWidthVisible = (currentViewRect.right - currentViewRect.left) >= view.getMeasuredWidth();
        boolean totalViewVisible = partVisible && totalHeightVisible && totalWidthVisible;
        // if any part of the view is clipped by any of its parents,return true
        if (!totalViewVisible)
            return true;

        while (currentView.getParent() instanceof ViewGroup) {
            ViewGroup currentParent = (ViewGroup) currentView.getParent();
            // if the parent of view is not visible,return true
            if (currentParent.getVisibility() != View.VISIBLE)
                return true;

            int start = indexOfViewInParent(currentView, currentParent);
            for (int i = start + 1; i < currentParent.getChildCount(); i++) {
                Rect viewRect = new Rect();
                view.getGlobalVisibleRect(viewRect);
                View otherView = currentParent.getChildAt(i);
                Rect otherViewRect = new Rect();
                otherView.getGlobalVisibleRect(otherViewRect);
                // if view intersects its older brother(covered),return true
                if (Rect.intersects(viewRect, otherViewRect))
                    return true;
            }
            currentView = currentParent;
        }
        return false;
    }

    private static int indexOfViewInParent(View view, ViewGroup parent) {
        int index;
        for (index = 0; index < parent.getChildCount(); index++) {
            if (parent.getChildAt(index) == view)
                break;
        }
        return index;
    }
}
