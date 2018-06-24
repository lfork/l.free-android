package com.lfork.a98620.lfree.base.bindingadapter;

import android.databinding.BindingAdapter;
import android.widget.ListView;

import com.lfork.a98620.lfree.base.adapter.ListViewAdapter;

import java.util.ArrayList;

/**
 * Created by 98620 on 2018/6/24.
 */
public class ListViewBinding {

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
            if (adapter.getViewModelList() == null) {
                adapter.setViewModelList(viewModels);
            }
            adapter.notifyDataSetChanged();
        }
    }
}