package com.lfork.a98620.lfree.main.myinfo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.MainMyInforFragBinding;
import com.lfork.a98620.lfree.main.MainActivity;

public class MyInforFragment extends Fragment {

    private View rootView;//页面缓存 ，这样做，当fragment被destroy的时候 可以让view不被回收，等下就可以快速恢复。

    private MyInforFragmentViewModel viewModel;

    public void refreshData(){
        if (viewModel != null){
            viewModel.refreshData();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            MainMyInforFragBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_my_infor_frag, container, false);
            viewModel = new MyInforFragmentViewModel((MainActivity) getActivity());
            binding.setViewModel(viewModel);
            rootView = binding.getRoot();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

}
