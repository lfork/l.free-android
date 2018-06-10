package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.data.entity.Category;

import java.util.ArrayList;
import java.util.List;

class PagerItemAdapter extends PagerAdapter {
    private List<Category> pageTitles;

    private List<PagerItemView> viewList = new ArrayList<>();//view数组

    private Context context;

    PagerItemAdapter(List<Category> pageTitles) {
        this.pageTitles = pageTitles;
    }

    //判断是否是由对象生成的界面
    @Override
    public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
        return arg0 == arg1;
    }

    public PagerItemView getPagerItemView(int index){
        Log.d("Thread1", "getPagerItemView: " + Thread.currentThread().getName());
        if (viewList.size() == 0) {
            return null;
        }
        return viewList.get(index);
    }


    /**
     * 这个 Count会影响 View的绘制，将会绘制的view个数，而不是已有的个数。不然就会出现加载不出来的情况。
     * @return  将会绘制的view个数
     */
    @Override
    public int getCount() {
        return pageTitles.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position,
                            @NonNull Object object) {
        container.removeView(viewList.get(position));
    }

    //返回一个对象 ，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup parent, int position) {
        PagerItemView itemView =  viewList.get(position);
        ViewGroup parent1 = (ViewGroup) itemView.getView().getParent();
        if (parent1 != null) {
            parent1.removeView(itemView.getView());   //提前加载view的原因，因为parent不一致，所以这里要替换一下parent
        }
        parent.addView(itemView.getView());
        return itemView.getView();
    }

    /**
     * 因为 viewPager根tabLayout只默认同步了 titles, 所以 这里的view预加载，就是为了 同步view而做的
     */
    private void preInitItemView(ViewGroup parent){
        for(Category title : pageTitles) {
            PagerItemView itemView = new PagerItemView(parent, title);
            viewList.add(itemView);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position).getName();
    }

    public void replaceData(List<Category> items, ViewPager parent) {
        this.context = parent.getContext();
        this.pageTitles.addAll(items);
        preInitItemView(parent);
        notifyDataSetChanged(); //先通知数据改变，再绑定数据
        Log.d("PagerItemAdapter", "replaceData: " + viewList.size());
    }


}