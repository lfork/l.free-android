package com.lfork.a98620.lfree.base.adapter;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.main.index.GoodsItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个通用的适配器只能接收 已经初始化好了的viewModel ， 如果需要在适配器里面进行相应
 * 的初始化，那么就需要继承自这个适配器或者自己另外写了
 *
 * 还有我也为这个通用适配器写了个通用的数据绑定，见{@link com.lfork.a98620.lfree.base.bindingadapter.ListBinding#refreshRecyclerView(RecyclerView, ArrayList)}
 * Created by 98620 on 2018/3/31.
 */

public class RecyclerViewItemAdapter<ViewModel extends BaseViewModel> extends RecyclerView.Adapter<RecyclerViewItemAdapter.ViewHolder> {
    private List<ViewModel> items;

    private Object navigator;

    private int layoutId;

    public RecyclerViewItemAdapter(int layoutId) {
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


    /**
     *  由于navigator的使用 可以让代码更容易维护 更清楚。所以在这里就加了个通用的设置navigator的操作。
     *  这样就可以更加方便的为适配器里面的viewModel设置精确的navigator
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewItemAdapter.ViewHolder holder, int position) {
        if (navigator != null) {  //兼容之前的代码，之前的有些代码是没有设置navigator的
            items.get(position).setNavigator(navigator);
        }

        holder.getBinding().setVariable(BR.viewModel, items.get(position));
        holder.getBinding().executePendingBindings();   //当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
    }


    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public List<ViewModel> getItems() {
        return items;
    }

    public void setItems(List<ViewModel> items) {
        notifyDataSetChanged();
        this.items = items;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        public ViewDataBinding getBinding() {
            return this.binding;
        }
    }

    protected int getLayoutId() {
        return layoutId;
    }

    public Object getNavigator() {
        return navigator;
    }

    public void setNavigator(Object navigator) {
        this.navigator = navigator;
    }

    /**
     * 因为加入了 View的引用 (Navigator) ，所以这里需要取消引用来避免将内存泄漏
     *
     * 这个必须要和activity的生命周期进行同步
     */
    public void onDestroy(){
        navigator = null;
    }
}
