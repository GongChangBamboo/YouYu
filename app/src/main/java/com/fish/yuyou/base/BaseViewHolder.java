package com.fish.yuyou.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zxy on 2018/08/20.
 */
public class BaseViewHolder {
    private SparseArray<View> mViews;
    protected View convertView;
    protected int position;
    protected Context context;


    protected BaseViewHolder(Context context, ViewGroup parent, int layoutID, int position) {
        this.context = context;
        this.position = position;
        this.mViews = new SparseArray<>();
        this.convertView = LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false);
        this.convertView.setTag(this);
    }

    /**
     * 得到一个holder
     */
    public static BaseViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new BaseViewHolder(context, parent, layoutId, position);
        } else {
            BaseViewHolder holder = (BaseViewHolder) convertView.getTag();
            return holder;
        }
    }

    /**
     * 通过id初始化控件
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }
    //****************************功能区 设置数据**********************************

    /**
     * 控制显隐
     */
    public BaseViewHolder controlVisible(int resId, Boolean visible) {
        TextView tv = getView(resId);
        if (visible) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 给TextView设值
     */
    public BaseViewHolder setText(int resId, String text) {
        TextView tv = getView(resId);
        tv.setText(text);
        return this;
    }

    /**
     * 给TextView设置背景颜色
     */
    public BaseViewHolder setColor(int resId, String color) {
        TextView tv = getView(resId);
        tv.setBackgroundColor(Color.parseColor(color));
        return this;
    }

    /**
     * 给TextView设置背景颜色
     */
    public BaseViewHolder setBgColor(int resId, String color) {
        LinearLayout ll = getView(resId);
        ll.setBackgroundColor(Color.parseColor(color));
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
            }
        });
        return this;
    }

    /**
     * 给ImageView设Bitmap
     */
    public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        }
        return this;
    }

    /**
     * 列表中checkbox
     */
    public BaseViewHolder setCheckBox(int viewId, boolean isSelect) {
        CheckBox cb = getView(viewId);
        cb.setChecked(isSelect);
        return this;
    }

    /**
     * 文件列表中checkbox显隐
     */
    public BaseViewHolder setFileCheckBox(int viewId, boolean isSelect) {
        CheckBox cb = getView(viewId);
        if (isSelect) {
            cb.setVisibility(View.VISIBLE);
        } else {
            cb.setVisibility(View.GONE);

        }
        cb.setChecked(isSelect);
        return this;
    }

    /**
     * edittext
     */
    public BaseViewHolder setEdittext(int viewId, String value) {
        EditText et = getView(viewId);
        et.setText(value);
        return this;
    }
}
