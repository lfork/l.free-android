package com.lfork.a98620.lfree.util.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 *
 * Created by 98620 on 2017/12/16.
 */

public class ListViewAdapter<T> extends BaseAdapter {

    private static final String TAG = "MyBaseAdapter";

    private Context context;
    private LayoutInflater inflater;
    private int layoutId;
    private int variableId;
    private List<T> viewModelList;

    public ListViewAdapter(Context context, int layoutId, List<T> list, int resId) {
        this.context = context;
        this.layoutId = layoutId;
        this.viewModelList = list;
        this.variableId = resId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding dataBinding;
        if (convertView == null) {
            dataBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        }else{
            dataBinding = DataBindingUtil.getBinding(convertView);
        }
        dataBinding.setVariable(variableId, viewModelList.get(position));
        return dataBinding.getRoot();
    }

    public int getCount() {
        return viewModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return viewModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
