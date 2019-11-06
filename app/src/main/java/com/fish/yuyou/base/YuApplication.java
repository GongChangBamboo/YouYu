package com.fish.yuyou.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class YuApplication extends Application {
    public static List<Activity> mActivityList = null;
    private static YuApplication instance;
    public static String token = "";

    public static YuApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mActivityList = new ArrayList<Activity>();
//        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        //初始化需要的工具类 如 sp packageutil
        initUtil();
    }


//    @SuppressLint("NewApi")
//    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
//
//        @Override
//        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onActivityStarted(Activity activity) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onActivityResumed(Activity activity) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onActivityPaused(Activity activity) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onActivityStopped(Activity activity) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onActivityDestroyed(Activity activity) {
//
//        }
//
//    };

    public static Context getContext() {
        return instance;
    }

    public static void finishActivitys() {
        for (int i = 0; i < mActivityList.size(); i++) {
            mActivityList.get(i).finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivityclass(Class<?> cls) {
        if (mActivityList != null) {
            for (Activity activity : mActivityList) {
                if (activity.getClass().equals(cls)) {
                    mActivityList.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }

    }

    public void finishActivity(Activity activity) {

        if (activity != null) {
            mActivityList.remove(activity);
            activity.finish();
        }
    }

    private void initUtil() {


    }

}
