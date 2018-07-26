package com.lfork.a98620.lfree.main.index;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.base.adapter.RecyclerViewItemAdapter;
import com.lfork.a98620.lfree.databinding.CommonGoodsRecycleItemBinding;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

class GoodsRecyclerViewItemAdapter extends RecyclerViewItemAdapter {

    private GoodsItemNavigator mGoodsItemNavigator;

    private PagerDataRefreshListener listener;


    GoodsRecyclerViewItemAdapter(GoodsItemNavigator mGoodsItemNavigator,int layoutId) {
        super(layoutId);
        this.mGoodsItemNavigator = mGoodsItemNavigator;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommonGoodsRecycleItemBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(parent.getContext()), getLayoutId(), parent, false);
        
        ViewHolder holder = new ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
        
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        GoodsItemViewModel viewModel = (GoodsItemViewModel)getItems().get(position);
        viewModel.setNavigator(mGoodsItemNavigator);

        if (position + 1 == getItemCount()) {
            listener.startRefreshing();
        }
        if (position > 10) {
            listener.onDown();
        }
    }

    //滑动到最后

    public PagerDataRefreshListener getListener() {
        return listener;
    }

    public void setListener(PagerDataRefreshListener listener) {
        this.listener = listener;
    }
}
