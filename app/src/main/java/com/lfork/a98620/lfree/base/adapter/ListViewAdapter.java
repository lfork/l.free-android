package com.lfork.a98620.lfree.base.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 *
 * Created by 98620 on 2017/12/16.
 */

public class ListViewAdapter<ViewModel> extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private int layoutId;
    private int variableId;
    private List<ViewModel> items;

    public ListViewAdapter(Context context, int layoutId, List<ViewModel> list, int resId) {
        this.context = context;
        this.layoutId = layoutId;
        this.items = list;
        this.variableId = resId;
        inflater = LayoutInflater.from(context);
    }

    public ListViewAdapter(Context context, int layoutId,int resId) {
        this.context = context;
        this.layoutId = layoutId;
        this.variableId = resId;
        inflater = LayoutInflater.from(context);
    }

    public List<ViewModel> getItems() {
        return items;
    }

    public void setItems(List<ViewModel> items) {
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding dataBinding;
        if (convertView == null) {
            dataBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        }else{
            dataBinding = DataBindingUtil.getBinding(convertView);
        }
        dataBinding.setVariable(variableId, items.get(position));
        return dataBinding.getRoot();
    }

    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
