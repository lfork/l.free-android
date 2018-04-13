package com.lfork.a98620.lfree.main.index;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.util.adapter.RecyclerViewItemAdapter;

import java.util.List;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

class GoodsRecyclerViewItemAdapter<T> extends RecyclerViewItemAdapter {

    GoodsRecyclerViewItemAdapter(List models, int layoutId) {
        super(models, layoutId);
    }
}
