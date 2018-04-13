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
import com.lfork.a98620.lfree.common.GlideImageLoader;
import com.lfork.a98620.lfree.databinding.MainIndexFragBinding;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment {

    private View rootView;// 缓存Fragment view@Override


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            MainIndexFragBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_index_frag, container, false);
            binding.setViewModel(new IndexFragmentViewModel(binding, getActivity(), getLayoutInflater()));
            rootView = binding.getRoot();
            initBanner();
        }
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;

    }

    private void initBanner(){
        Banner banner = rootView.findViewById(R.id.announcement_banner);
        List<String> images = new ArrayList<>();
        images.add("http://www.lfork.top/Test/2.png");
        images.add("http://www.lfork.top/Test/3.png");
        images.add("http://www.lfork.top/Test/4.png");
        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();
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
