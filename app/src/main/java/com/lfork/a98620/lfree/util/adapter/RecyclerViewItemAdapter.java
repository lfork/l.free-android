package com.lfork.a98620.lfree.util.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.BR;

import java.util.List;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class RecyclerViewItemAdapter<T> extends RecyclerView.Adapter<RecyclerViewItemAdapter.ViewHolder> {
    private List<T> items;

    private int layoutId;

    public RecyclerViewItemAdapter(List<T> models, int layoutId) {
        this.items = models;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(parent.getContext()), layoutId, parent, false);
        ViewHolder holder = new ViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewItemAdapter.ViewHolder holder, int position) {
        holder.getBinding().setVariable(BR.viewModel, items.get(position));
        holder.getBinding().executePendingBindings();   //当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
    }


    @Override
    public int getItemCount() {
        return items.size();
    }




    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return this.binding;
        }
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }
}
