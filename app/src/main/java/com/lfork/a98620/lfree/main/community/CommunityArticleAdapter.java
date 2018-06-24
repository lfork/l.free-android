package com.lfork.a98620.lfree.main.community;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.R;

import java.util.List;

public class CommunityArticleAdapter extends RecyclerView.Adapter<CommunityArticleAdapter.ViewHolder> {

    private List<CommunityArticle> itemViewModelList;

    public CommunityArticleAdapter(List<CommunityArticle> itemViewModelList) {
        this.itemViewModelList = itemViewModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(itemViewModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemViewModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        static ViewHolder create(LayoutInflater inflater, ViewGroup parent, int type) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_community_frag_recycler_view_item, parent, false);
            return new ViewHolder(binding);
        }

        public void bindTo(CommunityArticle itemViewModel) {
            binding.setVariable(BR.viewModel, itemViewModel);
            binding.executePendingBindings();
        }
    }
}
