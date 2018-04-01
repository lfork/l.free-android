package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.MainIndexFragBinding;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainIndexFragBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_index_frag, container, false);
        binding.setViewModel(new IndexFragmentViewModel(binding, getContext(), getLayoutInflater()));
        return binding.getRoot();
    }

    private static final String TAG = "A";


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

    }

}
