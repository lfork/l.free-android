package com.lfork.a98620.lfree.main.community;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

public class CommunityArticleAdapter extends RecyclerView.Adapter<CommunityArticleAdapter.ViewHolder> {

    private List<CommunityFragmentItemViewModel> articleList;
    private static Activity activity;

    public CommunityArticleAdapter(Activity activity, List<CommunityFragmentItemViewModel> articleList) {
        this.articleList = articleList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(articleList.get(position));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        static ViewHolder create(LayoutInflater inflater, ViewGroup parent, int type) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, type, parent, false);
            return new ViewHolder(binding);
        }

        public void bindTo(CommunityFragmentItemViewModel communityFragmentItemViewModel) {
            binding.setVariable(BR.viewModel, communityFragmentItemViewModel);
            binding.executePendingBindings();
        }
    }
}
