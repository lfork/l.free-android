package com.lfork.a98620.lfree.main.index;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;

import com.lfork.a98620.lfree.data.base.entity.Category;

import java.util.List;

/**
 * Created by 98620 on 2018/6/2.
 */
public class PagersDataBinding {
    @SuppressWarnings("unchecked")
    @BindingAdapter("tabs")
    public static void setTabs(ViewPager viewPager, List<Category> items) {
//        MyViewPagerAdapter adapter = (MyViewPagerAdapter) viewPager.getAdapter();
        PagerItemAdapter adapter = (PagerItemAdapter) viewPager.getAdapter();
        if (adapter != null)
        {
            adapter.replaceData(items, viewPager);
        }
    }
}
