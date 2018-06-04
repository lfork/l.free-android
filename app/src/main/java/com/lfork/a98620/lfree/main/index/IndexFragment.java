package com.lfork.a98620.lfree.main.index;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.image.GlideImageLoader;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.databinding.MainIndexFragBinding;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IndexFragment extends Fragment implements IndexFragmentNavigator, TabLayout.OnTabSelectedListener{

    private View rootView;// 缓存Fragment view@Override

    private ViewPager viewPager;

    private MainIndexFragBinding binding;

    private IndexFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.main_index_frag, container, false);
            viewModel = new IndexFragmentViewModel(binding, getActivity(), getLayoutInflater());
            binding.setViewModel(viewModel);
            rootView = binding.getRoot();
            setupBanner();
        }
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;

    }

    private void setupBanner(){
        Banner banner = rootView.findViewById(R.id.announcement_banner);
        List<String> images = new ArrayList<>();
        images.add("http://www.lfork.top/Test/2.png");
        images.add("http://www.lfork.top/Test/3.png");
        images.add("http://www.lfork.top/Test/4.png");
        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();
    }

    private void setupTabLayout(List<Category> categories){
        TabLayout tabLayout = binding.tabLayout;
        if (categories != null && categories.size() > 0) {
            for (Category category : categories) {
                TabItem item = new TabItem(Objects.requireNonNull(getContext()));
                item.setTag(category.getName());
                tabLayout.addView(item);
            }

        } else {
            Log.d(TAG, "addTabItem:  没有数据");
        }
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     * 这里设置了 viewPager以后 fragment需要持有viewPager的view的引用
     * @param categories 商品种类列表
     */
    private void setupViewPager(List<Category> categories) {
        viewPager = binding.mainIndexFragViewpager;
        IndexViewPagerAdapter pagerAdapter = new IndexViewPagerAdapter(categories);
        viewPager.setAdapter(pagerAdapter);

//        List<View> pagerList = new ArrayList<>();
//        pagerItemViewModelList = new ArrayList<>();
//        if (categories != null && categories.size() > 0) {
//            ViewDataBinding dataBinding;
//            for (Category category : categories) {
//                dataBinding = DataBindingUtil.inflate(inflater, R.layout.main_index_viewpager_item, viewPager, false);
//                IndexPagerItemViewModel itemViewModel = new IndexPagerItemViewModel(category, dataBinding, context);
//                dataBinding.setVariable(BR.viewModel, itemViewModel);
//                pagerList.add(dataBinding.getRoot());
//                pagerItemViewModelList.add(itemViewModel);
//            }
//            viewPager = binding.mainIndexFragViewpager;
//            PagerAdapter pagerAdapter = new IndexViewPagerAdapter(pagerList, categories);
//            viewPager.setAdapter(pagerAdapter);
//        } else {
//            Log.d(TAG, "addTabItem:  没有数据");
//        }
    }


    private static final String TAG = "A";


    @Override
    public void onCategoriesLoaded(List<Category> categories) {
        setupViewPager(categories);
        setupTabLayout(categories);
    }


    /**
     * 先加载前3个页面，当点击到指定页面的时候就加载指定的前后三个页面的数据
     *
     *
     *
     * @param tab 被选中的标签
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        PagerItemView pagerItemView = ((IndexViewPagerAdapter) Objects.requireNonNull(viewPager.getAdapter())).getPagerItemView(position);
        pagerItemView.onResume();
//        viewPager.getAdapter()  //获取指定页的viewModel 然后进行数据操作
        //获取当前页面数据
//        IndexPagerItemViewModel itemViewModel = pagerItemViewModelList.get(position);
//        if (itemViewModel != null) {
//            itemViewModel.initData();
//        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        PagerItemView pagerItemView = ((IndexViewPagerAdapter) Objects.requireNonNull(viewPager.getAdapter())).getPagerItemView(position);
//        pagerItemView.onDestroy();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}
