package com.lfork.a98620.lfree.main.community;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.R;

import java.util.ArrayList;
import java.util.List;

public class ArticleImgAdapter extends ArrayAdapter<CommunityFragmentImgItemViewModel> {

    private List<CommunityFragmentImgItemViewModel> articleImgViewModelList = new ArrayList<>();
    private int layoutId;
    private Context context;

    public ArticleImgAdapter(Context context,int layoutId, List<CommunityFragmentImgItemViewModel> articleImgViewModelList) {
        super(context,layoutId, articleImgViewModelList);
        this.context = context;
        this.layoutId = layoutId;
        this.articleImgViewModelList = articleImgViewModelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewDataBinding binding = null;
        if (convertView ==null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, parent, false);
        } else {
            binding = DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(BR.viewModel, articleImgViewModelList.get(position));
        binding.executePendingBindings();
        return binding.getRoot();
    }
}
