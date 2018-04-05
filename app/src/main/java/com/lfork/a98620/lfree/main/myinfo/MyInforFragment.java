package com.lfork.a98620.lfree.main.myinfo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.MainMyInforFragBinding;

public class MyInforFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainMyInforFragBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_my_infor_frag, container, false);
        binding.setViewModel(new MyInforFragmentViewModel(binding, getContext(), getLayoutInflater()));
        return binding.getRoot();
    }

}
