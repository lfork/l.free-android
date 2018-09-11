package com.lfork.a98620.lfree.main.index;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.base.entity.Category;
import com.lfork.a98620.lfree.databinding.MainIndexViewpagerItemBinding;
import com.lfork.a98620.lfree.goodsdetail.GoodsDetailActivity;
import com.lfork.a98620.lfree.util.ToastUtil;

/**
 * Created by 98620 on 2018/6/4.
 */
public class PagerItemView extends View implements PagerDataRefreshListener, SwipeRefreshLayout.OnRefreshListener, GoodsItemNavigator, PagerItemViewNavigator {

    private PagerItemViewModel viewModel;

    private static final String TAG = "PagerItemView";

    /**
     * 优化策略
     * 1、gc的“活对象”回收策略
     * 2、static 内存泄漏问题  application context内存泄漏问题(把要回收的context给和application同生命
     *  周期的class可能会阻止gc的回收)
     * 3、潜在的内存泄漏  Context的引用 ，Activity的引用  (注意：不是一定会内存泄漏)
     * 4、缓存策略：常驻内存操作 这样可以提升加载速度
     */
    private  Activity activityContext;

    private MainIndexViewpagerItemBinding binding;

    private boolean needForceRefresh = false;

    private boolean viewWasCreated = false;

    private View rootView;

    public View onCreateView(ViewGroup parent, Category category) {
        if (rootView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater
                    .from(parent.getContext()), R.layout.main_index_viewpager_item, parent, false);
            viewModel = new PagerItemViewModel(parent.getContext(), category);
            binding.setViewModel(viewModel);
            viewModel.setNavigator(this);
            setupRecyclerView();
            setupSwipeRefreshLayout();
            setupUpButton();
            rootView = binding.getRoot();
            viewWasCreated = true;

            Log.d(TAG, "onCreateView: the view was created" + category);
        }

        ViewGroup parent2 = (ViewGroup) rootView.getParent();
        if (parent2 != null) {
            parent2.removeView(rootView);
        }
        return rootView;
    }

    public void onResume() {
        if (viewModel != null) {
            viewModel.setNavigator(this);
            viewModel.start();
        }
    }

    public void onPause(){
        //do nothing here
    }

    /**
     * 因为 这里是需要缓存 的，所以在这里也不要随便onDestroy
     */
    public void onDestroy() {
        activityContext = null;
        viewModel.onDestroy();
    }

    public PagerItemView(Context context) {
        super(context);
    }

    public PagerItemView(ViewGroup parent) {
        this(parent.getContext());
    }


    public View getView() {
        if (binding != null) {
            return binding.getRoot();
        }
        return null;
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.mainIndexRecycle;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        GoodsRecyclerViewItemAdapter adapter
                = new GoodsRecyclerViewItemAdapter(this,R.layout.common_goods_recycle_item);
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new MyItemDecoration(40, getContext(), getResources().getColor(R.color.sub_background)));
        adapter.setListener(this);
    }

    private void setupSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener(this); //刷新监听
    }

    private void setupUpButton(){
        binding.up.setOnClickListener(v -> {
            binding.mainIndexRecycle.scrollToPosition(0);//将recyclerView定位到最后一行
            viewModel.isDown.set(false);
        });
    }

    @Override
    public void startRefreshing() {
        viewModel.startRefreshing();
    }

    @Override
    public void endRefresh() {
        viewModel.endRefresh();
    }

    @Override
    public void onDown() {
        viewModel.onDown();
    }

    /**
     * SwipeRefreshLayout的OnRefresh 这里就需要调用viewModel的一些操作进行数据刷新了
     */
    @Override
    public void onRefresh() {
        viewModel.refreshData();
    }

    public Activity getActivityContext() {
        return activityContext;
    }

    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
    }


    /**
     * 有3种情况
     * 1、当前页面(只会调用OnTabSelected)
     * 2、已经初始化了的隔壁页面 会先调用 OnTabSelected 然后调用 instantiateItem
     * 3、未初始化的页面  会先调用 OnTabSelected 然后调用 instantiateItem  
     */
    public void forceRefresh(){     //
        Log.d(TAG, "forceRefresh: " + needForceRefresh);
        if (needForceRefresh && !viewModel.dataIsLoading.get()) {  //没有被初始化 也不需要强制刷新
            binding.swipeRefresh.setRefreshing(true);
            onRefresh();
            needForceRefresh = false;
        }
    }


    public void setNeedForceRefresh(boolean needForceRefresh) {
        if (viewModel == null || viewModel.isInitialized()) {
            this.needForceRefresh = needForceRefresh;
        }
    }

    public boolean isViewWasCreated() {
        return viewWasCreated;
    }

    @Override
    public void openGoodsDetail(int goodsId, int categoryId) {
        GoodsDetailActivity.startActivity(activityContext, goodsId, categoryId );
    }

    @Override
    public void refreshEnd() {
        getActivityContext().runOnUiThread(() -> {
            binding.swipeRefresh.setRefreshing(false);
        });


    }

    @Override
    public void showToast(String msg) {
        getActivityContext().runOnUiThread(() -> {
            ToastUtil.showShort(getContext(), msg);

        });
    }
}
