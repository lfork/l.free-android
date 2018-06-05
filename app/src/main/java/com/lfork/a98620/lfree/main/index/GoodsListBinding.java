package com.lfork.a98620.lfree.main.index;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.lfork.a98620.lfree.data.entity.Goods;

import java.util.List;

/**
 * Created by 98620 on 2018/6/2.
 */
public class GoodsListBinding {
    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, List<Goods> items) {
        GoodsRecyclerViewItemAdapter adapter = (GoodsRecyclerViewItemAdapter) recyclerView.getAdapter();
        if (adapter != null)
        {
            adapter.setItems(items);
        }
    }
}
