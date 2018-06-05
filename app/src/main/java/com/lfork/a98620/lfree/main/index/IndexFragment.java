package com.lfork.a98620.lfree.main.index;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.lfork.a98620.lfree.searchresult.SearchResultActivity;
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
            viewModel = new IndexFragmentViewModel(getContext());

            binding.setViewModel(viewModel);
            rootView = binding.getRoot();
            setupBanner();
            setupViewPager();
            setupTabLayout();

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

    /**
     * 这里设置了 viewPager以后 fragment需要持有viewPager的view的引用
     */
    private void setupViewPager() {
        viewPager = binding.mainIndexFragViewpager;
        PagerItemAdapter pagerAdapter = new PagerItemAdapter(new ArrayList<Category>(0));
        viewPager.setAdapter(pagerAdapter);
        Log.d(TAG, "设置viewPager" + pagerAdapter.getCount());
    }

    /**
     * 必须先设置viewPager 在设置TabLayout
     */
    private void setupTabLayout(){
        TabLayout tabLayout = binding.tabLayout;
        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
    }


    private static final String TAG = "A";


    @Override
    public void openSearchActivity() {
        Log.d(TAG, "openSearch: ");
        Intent intent = new Intent(getContext(), SearchResultActivity.class);
        intent.putExtra("recommend_keyword", "Java从入门到精通");
        Objects.requireNonNull(getContext()).startActivity(intent);
    }

    /**
     * 先加载前3个页面，当点击到指定页面的时候就加载指定的前后三个页面的数据
     * @param tab 被选中的标签
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        PagerItemView pagerItemView = ((PagerItemAdapter) Objects.requireNonNull(viewPager.getAdapter())).getPagerItemView(position);
        Log.d(TAG, "onTabSelected: " + pagerItemView + "  " + position);
        if (pagerItemView != null) {    //如果TabLayout没有初始完完毕，这里可能会报空// 这里viewPager自身和tabLayout 不同步导致的
            pagerItemView.setActivityContext(getActivity());
            pagerItemView.onResume();
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        PagerItemView pagerItemView = ((PagerItemAdapter) Objects.requireNonNull(viewPager.getAdapter())).getPagerItemView(position);
        pagerItemView.onDestroy();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * 因为对viewModel和view进行了缓存， 所以 这里就需要重新设置一下navigator
     * 然后这里是不进行自动刷新数据的。所以onResume的时候就不调用OnStart
     */
    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.setNavigator(this);
            viewModel.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}
