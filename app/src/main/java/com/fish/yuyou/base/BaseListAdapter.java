package com.fish.yuyou.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zxy on 2018/08/20.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<T> list;
    private LayoutInflater inflater;
    private int mLayoutId;


    public BaseListAdapter(Context context, List<T> datas) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = datas;
    }

    /**
     * 绑定ListView 的item布局
     */
    public void bindLayout(int resId) {
        mLayoutId = resId;
    }

    @Override
    public int getCount() {
        return this.list != null ? this.list.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return this.list != null && position < this.list.size() ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = BaseViewHolder.get(context, convertView, parent, mLayoutId, position);
        convert(holder, getItem(position), position);
        return holder.getConvertView();
    }

    protected abstract void convert(BaseViewHolder holder, T t, int position);
}