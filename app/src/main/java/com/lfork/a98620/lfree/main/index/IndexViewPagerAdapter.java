package com.lfork.a98620.lfree.main.index;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.data.entity.Category;

import java.util.ArrayList;
import java.util.List;

class IndexViewPagerAdapter extends PagerAdapter {

    private final ArrayList<Category> pageTitles;

    private List<View> viewList;//view数组

    public IndexViewPagerAdapter(List<View> viewList, ArrayList<Category> pageTitles) {
        this.viewList = viewList;
        this.pageTitles = pageTitles;
    }

    //判断是否是由对象生成的界面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return viewList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        // TODO Auto-generated method stub
        container.removeView(viewList.get(position));
    }

    //返回一个对象 ，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position).getName();
    }
}