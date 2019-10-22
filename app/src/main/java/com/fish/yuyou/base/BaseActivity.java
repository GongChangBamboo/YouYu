package com.fish.yuyou.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;


public abstract class BaseActivity extends FragmentActivity {

    protected Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        YuApplication.mActivityList.add(this);


    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected void setDefaultInit() {
        initView();
        initData();
        initListener();
    }


    protected void toActivity(Class clazz) {
        Intent i = new Intent(mContext, clazz);
        startActivity(i);
    }

    protected String ignoreEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YuApplication.mActivityList.remove(this);
    }
}
