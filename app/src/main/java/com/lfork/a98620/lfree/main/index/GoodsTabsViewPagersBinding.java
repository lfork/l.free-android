package com.lfork.a98620.lfree.main.index;

import android.databinding.BindingAdapter;
import android.support.v4.view.ViewPager;

import com.lfork.a98620.lfree.data.entity.Category;

import java.util.List;

/**
 * Created by 98620 on 2018/6/2.
 */
public class GoodsTabsViewPagersBinding {
    @SuppressWarnings("unchecked")
    @BindingAdapter("items")
    public static void setItems(ViewPager viewPager, List<Category> items) {
        IndexViewPagerAdapter adapter = (IndexViewPagerAdapter) viewPager.getAdapter();
        if (adapter != null)
        {
            adapter.replaceData(items);
        }
    }
}
