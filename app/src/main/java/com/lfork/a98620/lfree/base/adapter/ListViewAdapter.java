package com.lfork.a98620.lfree.base.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lfork.a98620.lfree.base.BaseViewModel;

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

public class ListViewAdapter<ViewModel extends BaseViewModel> extends BaseAdapter {

    private LayoutInflater inflater;
    private int layoutId;
    private int variableId;
    private List<ViewModel> items;

    private Object navigator;


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


    /**
     *  由于navigator的使用 可以让代码更容易维护 更清楚。所以在这里就加了个通用的设置navigator的操作。
     *  这样就可以更加方便的为适配器里面的viewModel设置精确的navigator
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding dataBinding;
        if (convertView == null) {
            dataBinding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        }else{
            dataBinding = DataBindingUtil.getBinding(convertView);
        }
        if (navigator != null) { //兼容以前的代码
            items.get(position).setNavigator(navigator);
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

    /**
     * 因为加入了 View的引用 (Navigator) ，所以这里需要取消引用来避免将内存泄漏
     */
    public void onDestroy(){
        navigator = null;
    }

    public Object getNavigator() {
        return navigator;
    }

    public void setNavigator(Object navigator) {
        this.navigator = navigator;
    }
}
