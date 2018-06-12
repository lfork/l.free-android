package com.lfork.a98620.lfree.articlecontent;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.R;

import java.util.ArrayList;
import java.util.List;


public class CommunityCommentAdapter extends RecyclerView.Adapter<CommunityCommentAdapter.ViewHolder> {

    private List<CommunityComment> viewModelList = new ArrayList<>();

    public CommunityCommentAdapter(List<CommunityComment> viewModelList) {
        this.viewModelList = viewModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.create(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(viewModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        static ViewHolder create(LayoutInflater inflater, ViewGroup parent, int type) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.community_detail_comment_item, parent, false);
            return new ViewHolder(binding);
        }

        void bindTo(CommunityComment itemViewModel) {
            binding.setVariable(BR.viewModel, itemViewModel);
            binding.executePendingBindings();
        }
    }
}
