package com.lfork.a98620.lfree.main.index;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.databinding.GoodsRecycleItemBinding;
import com.lfork.a98620.lfree.util.adapter.RecyclerViewItemAdapter;

import java.util.List;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

class GoodsRecyclerViewItemAdapter<T> extends RecyclerViewItemAdapter {

    private static final String TAG = "GoodsRecyclerViewItemAd";

    private GoodsItemNavigator mGoodsItemNavigator;

    private PagerDataRefreshListener listener;

    GoodsRecyclerViewItemAdapter(List<T> models, int layoutId) {
        super(models, layoutId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GoodsRecycleItemBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(parent.getContext()), getLayoutId(), parent, false);
        
        ViewHolder holder = new ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
        
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GoodsItemViewModel viewmodel = new GoodsItemViewModel(holder.getBinding().getRoot().getContext(),
                (Goods)getItems().get(position)
        );
        ((GoodsRecycleItemBinding)holder.getBinding()).setViewModel(viewmodel);
        viewmodel.setNavigator(getmGoodsItemNavigator());
        if (position + 1 == getItemCount()) {
            listener.startRefreshing();
        }
    }

    //滑动到最后


    public PagerDataRefreshListener getListener() {
        return listener;
    }

    public void setListener(PagerDataRefreshListener listener) {
        this.listener = listener;
    }

    public GoodsItemNavigator getmGoodsItemNavigator() {
        return mGoodsItemNavigator;
    }

    public void setGoodsItemNavigator(GoodsItemNavigator mGoodsItemNavigator) {
        this.mGoodsItemNavigator = mGoodsItemNavigator;
    }
}
