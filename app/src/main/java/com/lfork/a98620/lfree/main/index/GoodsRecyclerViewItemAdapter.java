package com.lfork.a98620.lfree.main.index;

import android.support.annotation.NonNull;

import com.lfork.a98620.lfree.util.adapter.RecyclerViewItemAdapter;

import java.util.List;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

class GoodsRecyclerViewItemAdapter<T> extends RecyclerViewItemAdapter {

    private static final String TAG = "GoodsRecyclerViewItemAd";

    private DataRefreshListener listener;

    GoodsRecyclerViewItemAdapter(List models, int layoutId) {
        super(models, layoutId);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (position + 1 == getItemCount()) {
            listener.startRefreshing();
        }
    }

    //滑动到最后


    public DataRefreshListener getListener() {
        return listener;
    }

    public void setListener(DataRefreshListener listener) {
        this.listener = listener;
    }
}
