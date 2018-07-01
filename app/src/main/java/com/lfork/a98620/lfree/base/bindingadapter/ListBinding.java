package com.lfork.a98620.lfree.base.bindingadapter;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.lfork.a98620.lfree.base.adapter.ListViewAdapter;
import com.lfork.a98620.lfree.base.adapter.RecyclerViewItemAdapter;

import java.util.ArrayList;

/**
 * 通用的 ListView和RecyclerView的数据绑定
 * Created by 98620 on 2018/6/24.
 */
public class ListBinding {

    /**
     * 这个方法只适合一般需求的listView ，
     * 并且adapter是用的通用adapter {@link com.lfork.a98620.lfree.base.adapter.ListViewAdapter}
     *
     * @param listView   listView
     * @param viewModels 各种类型的viewModel
     */
    @BindingAdapter({"setListViewItems"})
    public static void refreshListView(ListView listView, ArrayList viewModels) {
        ListViewAdapter adapter = (ListViewAdapter) listView.getAdapter();
        if (viewModels == null) {
            return;
        }
        if (adapter != null) {
            adapter.setItems(viewModels);
            adapter.notifyDataSetChanged();
        }
    }

    @BindingAdapter({"setRecyclerViewItems"})
    public static void refreshRecyclerView(RecyclerView listView, ArrayList viewModels) {
        RecyclerViewItemAdapter  adapter = (RecyclerViewItemAdapter) listView.getAdapter();
        if (viewModels == null) {
            return;
        }
        if (adapter != null) {
            adapter.setItems(viewModels);
            adapter.notifyDataSetChanged();
        }
    }
}