package com.lfork.a98620.lfree.main.index;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.data.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class MyViewPagerAdapter extends PagerAdapter {

    final String[] pagetitles = {"政治", "体育", "教育", "汽车", "科技", "手机", "娱乐", "财经", "社会", "军事"};


//    private List<Category> pagetitles;

    private List<PagerItemView> viewList2 = new ArrayList<>();//view数组

    private Activity context;

    private static final String TAG = "MyViewPagerAdapter";
    private List<View> viewList;//view数组

    public MyViewPagerAdapter() {
        this.viewList = new ArrayList<>();
    }

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
        Log.d(TAG, "destroyItem: " + position);
        container.removeView(viewList.get(position));
    }

    //返回一个对象 ，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub

//            if (viewList.size() < 1) {
//                return null;
//            }
        container.addView(viewList.get(position));
        Log.d(TAG, "instantiateItem: " + position);
        return viewList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagetitles[position];
//                return super.getPageTitle(position);
    }

    public void replaceData(List<Category> items, ViewPager parent) {
//        this.pageTitles.addAll(items);
        preInitItemView(parent);
        notifyDataSetChanged(); //先通知数据改变，再绑定数据
    }

    private void preInitItemView(ViewGroup parent) {
        for (int i = 0; i < 10;i++) {
            PagerItemView itemView = new PagerItemView(parent);
            viewList.add(itemView);
        }
    }
}