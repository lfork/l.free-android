package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.MainIndexFragBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 98620 on 2018/3/31.
 */

public class IndexFragmentViewModel implements TabLayout.OnTabSelectedListener {

    private static final String TAG = "IndexFragmentViewModel";
    private String[] categories = {"推荐", "电子产品", "书本信息", "生活用品", "其他1", "其他2", "其他3", "其他4", "其他5", "其他6"};
    ;
    private List<View> pagerList;
    private MainIndexFragBinding binding;
    private Context context;
    private LayoutInflater inflater;
    private ViewPager viewPager;

    IndexFragmentViewModel(MainIndexFragBinding binding, Context context, LayoutInflater layoutInflater) {
        this.binding = binding;
        this.context = context;
        this.inflater = layoutInflater;
        initViewPager();
        initTabLayout();
    }

    private void initTabLayout() {

        TabLayout tabLayout = binding.tabLayout;
        if (categories != null && categories.length > 0) {
            for (String category : categories) {
                TabItem item = new TabItem(context);
                item.setTag(category);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                    }
//                });
//                tableRow.addView(button);
                tabLayout.addView(item);
            }

        } else {
            Log.d(TAG, "addTabItem:  没有数据");
        }


        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);

    }


    private void initViewPager() {
        pagerList = new ArrayList<>();
        if (categories != null && categories.length > 0) {
            ViewDataBinding dataBinding;
            for (String category : categories) {
                dataBinding = DataBindingUtil.inflate(inflater, R.layout.main_index_viewpager_item, viewPager, false);
                dataBinding.setVariable(BR.viewModel, new IndexPagerItemViewModel(category, dataBinding,context));
                pagerList.add(dataBinding.getRoot());
            }
            viewPager = binding.mainIndexFragViewpager;
            PagerAdapter pagerAdapter = new IndexViewPagerAdapter(pagerList);
            viewPager.setAdapter(pagerAdapter);
        } else {
            Log.d(TAG, "addTabItem:  没有数据");
        }
//        View view1 = inflater.inflate(R.layout.main_index_viewpager_item, null);
//        View view2 = inflater.inflate(R.layout.main_index_viewpager_item, null);
//        View view3 = inflater.inflate(R.layout.main_index_viewpager_item, null);
//        View view4 = inflater.inflate(R.layout.main_index_viewpager_item, null);
//        View view5 = inflater.inflate(R.layout.main_index_viewpager_item, null);
//        View view6 = inflater.inflate(R.layout.main_index_viewpager_item, null);
//        View view7 = inflater.inflate(R.layout.main_index_viewpager_item, null);
//        View view8 = inflater.inflate(R.layout.main_index_viewpager_item, null);
//        View view9 = inflater.inflate(R.layout.main_index_viewpager_item, null);
//        View view10 = inflater.inflate(R.layout.main_index_viewpager_item, null);
//
//
//        pagerList.add(view2);
//        pagerList.add(view3);
//        pagerList.add(view4);
//        pagerList.add(view5);
//        pagerList.add(view6);
//        pagerList.add(view7);
//        pagerList.add(view8);
//        pagerList.add(view9);
//        pagerList.add(view10);

    }


    private void onTabClicked() {
        //进行viewPager的切换
    }

    /**
     *
     */
    private void loadPagerItem() {
        //把当前的viewPager 进行初始化， 加载数据列表
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
