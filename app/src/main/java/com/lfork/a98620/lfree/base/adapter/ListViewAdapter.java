package com.lfork.a98620.lfree.base.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 98620 on 2017/12/16.
 *
 * /**
 * 这个通用的适配器只能接收 已经初始化好了的viewModel ， 如果需要在适配器里面进行相应
 * 的初始化，那么就需要继承自这个适配器或者自己另外写了
 *
 * 还有我也为这个通用适配器写了个通用的数据绑定，见{@link com.lfork.a98620.lfree.base.bindingadapter.ListBinding#refreshListView(ListView, ArrayList)}
 * Created by 98620 on 2018/3/31.
 *
 */

public class ListViewAdapter<ViewModel> extends BaseAdapter {

    private LayoutInflater inflater;
    private int layoutId;
    private int variableId;
    private List<ViewModel> items;


    public ListViewAdapter(Context context, int layoutId,int resId) {
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
        if (items == null) {
            return  0;
        }
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
