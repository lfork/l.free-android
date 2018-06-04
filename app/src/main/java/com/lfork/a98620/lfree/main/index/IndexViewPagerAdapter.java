package com.lfork.a98620.lfree.main.index;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.data.entity.Category;

import java.util.ArrayList;
import java.util.List;

class IndexViewPagerAdapter extends PagerAdapter {

    private List<Category> pageTitles;


    private List<PagerItemView> viewList = new ArrayList<>();//view数组

    public IndexViewPagerAdapter(List<View> viewList, List<Category> pageTitles) {
//        this.viewList = viewList;
        this.pageTitles = pageTitles;
    }

    public IndexViewPagerAdapter(List<Category> pageTitles) {
        this.pageTitles = pageTitles;
    }

    //判断是否是由对象生成的界面
    @Override
    public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
        return arg0 == arg1;
    }

    public PagerItemView getPagerItemView(int index){
        return viewList.get(index);
    }

    @Override
    public int getCount() {
        return viewList.size();
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
        PagerItemView itemView = new PagerItemView(parent);
//        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
//                .from(parent.getContext()), R.layout.main_index_viewpager_item, parent, false);

//        ViewDataBinding binding2 = DataBindingUtil.in
//        IndexPagerItemViewModel viewModel = new IndexPagerItemViewModel();
//        viewModels
//        RecyclerViewItemAdapter.ViewHolder holder = new RecyclerViewItemAdapter.ViewHolder(binding.getRoot());
//        holder.setBinding(binding);
//        return holder;
//        //临时生成一个view
//        container.addView(viewList.get(position));
//        return viewList.get(position);

        //临时生成一个view
        parent.addView(itemView.getView());
        viewList.add(itemView);
//        return binding.getRoot();

        return itemView.getView();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position).getName();
    }

    public void replaceData(List<Category> items) {
        this.pageTitles.addAll(items);
    }
}