package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.databinding.MainIndexFragBinding;
import com.lfork.a98620.lfree.searchresult.SearchResultActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class IndexFragmentViewModel  {

    private static final String TAG = "IndexFragmentViewModel";
    public final ObservableArrayList<Category> categories = new ObservableArrayList<>();

    private IndexFragmentNavigator navigator;

    private MainIndexFragBinding binding;
    private FragmentActivity context;
    private LayoutInflater inflater;
    private ViewPager viewPager;
    private GoodsDataRepository repository;
    private ArrayList<IndexPagerItemViewModel> pagerItemViewModelList;
    public final ObservableBoolean dataIsLoading = new ObservableBoolean(true);

    IndexFragmentViewModel(MainIndexFragBinding binding, FragmentActivity context, LayoutInflater layoutInflater) {
        this.binding = binding;
        this.context = context;
        this.inflater = layoutInflater;
//        binding.searchBtn.clearFocus(); //取消searchView的焦点
        initData();
    }

    IndexFragmentViewModel(Context context) {

    }

    public void start(){
        initData();
    }

    private void initData() {
        repository = GoodsDataRepository.getInstance();
        new Thread(() -> {
            repository.getCategories(new DataSource.GeneralCallback<List<Category>>() {
                @Override
                public void succeed(List<Category> data) {
                    context.runOnUiThread(() -> {
                        categories.clear();
                        categories.addAll(data);
                        if (navigator != null) {
                            navigator.onCategoriesLoaded(categories);
                        }
                        dataIsLoading.set(false);
                    });
                }

                @Override
                public void failed(String log) {
                    context.runOnUiThread(() -> {
                        Toast.makeText(context, log, Toast.LENGTH_LONG).show();
                    });
                }
            });
        }).start();


    }

//    private void initTabLayout() {
//        TabLayout tabLayout = binding.tabLayout;
//        if (categories != null && categories.size() > 0) {
//            for (Category category : categories) {
//                TabItem item = new TabItem(context);
//                item.setTag(category.getName());
//                tabLayout.addView(item);
//            }
//
//        } else {
//            Log.d(TAG, "addTabItem:  没有数据");
//        }
//
//        tabLayout.addOnTabSelectedListener(this);
//        tabLayout.setupWithViewPager(viewPager);
//
//    }
//
//
//    private void initViewPager() {
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
//
//    }

    public void openSearch(){
        Log.d(TAG, "openSearch: ");
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra("recommend_keyword", "Java从入门到精通");
        context.startActivity(intent);
    }



    public void setNavigator(IndexFragmentNavigator navigator) {
        this.navigator = navigator;
    }

    public void onDestroy(){
        this.navigator = null;
    }
}
