package com.lfork.a98620.lfree.main.index;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

public class IndexFragment extends Fragment implements IndexFragmentNavigator,TabLayout.OnTabSelectedListener{

    private View rootView;// 缓存Fragment view@Override

    private ViewPager viewPager;

    private MyViewPagerAdapter adapter2;

    private PagerItemAdapter pagerAdapter;

    private MainIndexFragBinding binding;

    private IndexFragmentViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        adapter2 = new MyViewPagerAdapter();
        pagerAdapter = new PagerItemAdapter(new ArrayList<Category>(0), getActivity());
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * 先设置viewPager 再设置TabLayout
     */
    private void setupTabLayout(){
        TabLayout tabLayout = binding.tabLayout;
//        tabLayout.addOnTabSelectedListener(new TabSelectedListener());
        tabLayout.addOnTabSelectedListener(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    private static final String TAG = "A";


    @Override
    public void openSearchActivity() {
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
        if (pagerItemView != null) {    //如果TabLayout没有初始完完毕，这里可能会报空// 这里viewPager自身和tabLayout 不同步导致的
            pagerItemView.setActivityContext(getActivity());
            pagerItemView.onResume();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        PagerItemView pagerItemView = ((PagerItemAdapter) Objects.requireNonNull(viewPager.getAdapter())).getPagerItemView(position);
        pagerItemView.onPause();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        ((PagerItemAdapter) Objects.requireNonNull(viewPager.getAdapter())).getPagerItemView(tab.getPosition()).onResume();
    }

    /**
     * 在onResume里面设置对view的引用 在{@link #onDestroy()}里面取消view的引用
     *
     * Navigator里面主要有两种操作
     * 1、用户的主动交互 (比如点击) ->执行viewModel里面的方法  。viewModel再直接通过navigator来进行相应。
     * 2、数据加载完成后的操作：由于使用了BindingAdapter和mvvm自带的DataBinding所以一般简单的数据操作完成后
     *   navigator最多就做个提示(Toast,SnackBar)即可
     *
     *   如果是加载复杂的数据：比如要根据新加载的数据来加载新的view(eg: {@link PagerItemView}) 这个就是
     *   MVVM的BindingAdapter不能胜任的了
     *
     * 为什么不在onPause里面取消呢？ 因为当前的view如果没有执行onDestroy的话说明这个View很可能还是会再次调用的
     * 然后及时当前的view不可见了之后，然后viewModel通过navigator来做的
     */
    @Override
    public void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.setNavigator(this);   //恢复到当前的UI，重新设置view
            viewModel.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}
