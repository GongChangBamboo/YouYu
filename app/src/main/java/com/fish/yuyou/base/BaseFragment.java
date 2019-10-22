package com.fish.yuyou.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public abstract class BaseFragment extends Fragment {

    public Activity mActivity;

    //防止 出现getActivity为null的情况
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity)context;
    }

    /**
     * Fragment创建
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mActivity = getActivity();
    }

    /**
     * Fragment布局
     *
     * return 返回Fragment应该填充的View对象
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    /**
     * Activity创建完成
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    /**
     * 初始化界面 该方法必须实现!
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public void initData() {

    }
    /**
     * 初始化监听
     */
    public void initListener() {

    }
    protected void toActivity(Class clazz) {
        Intent i = new Intent(mActivity, clazz);
        startActivity(i);
    }
}