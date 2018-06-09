package com.lfork.a98620.lfree.main.index;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.databinding.MainIndexViewpagerItemBinding;
import com.lfork.a98620.lfree.goodsdetail.GoodsDetailActivity;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by 98620 on 2018/6/4.
 */
public class PagerItemView extends View implements PagerDataRefreshListener, SwipeRefreshLayout.OnRefreshListener, GoodsItemNavigator, PagerItemViewNavigator {

    private PagerItemViewModel viewModel;

    private Activity activityContext;

    private MainIndexViewpagerItemBinding binding;

    public PagerItemView(Context context) {
        super(context);
    }


    public PagerItemView(ViewGroup parent, Category category) {
        this(parent.getContext());
        onCreate(parent, category);
    }


    public void onCreate(ViewGroup parent, Category category) {
        binding = DataBindingUtil.inflate(LayoutInflater
                .from(parent.getContext()), R.layout.main_index_viewpager_item, parent, false);
        viewModel = new PagerItemViewModel(parent.getContext(), category);
        binding.setViewModel(viewModel);
        viewModel.setNavigator(this);
        setupRecyclerView();
        setupSwipeRefreshLayout();

    }

    public void onResume() {
        if (viewModel != null) {
            viewModel.setNavigator(this);
            viewModel.start();
        }
    }

    /**
     * 因为 这里是需要缓存 的，所以在这里也不要随便onDestroy
     */
    public void onDestroy() {
        activityContext = null;
        viewModel.destroy();
    }


    public View getView() {
        if (binding != null) {
            return binding.getRoot();
        }
        return null;
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.mainIndexRecycle;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        GoodsRecyclerViewItemAdapter adapter = new GoodsRecyclerViewItemAdapter<>(new ArrayList<Goods>(0), R.layout.goods_recycle_item);
        recyclerView.setAdapter(adapter);
        adapter.setGoodsItemNavigator(this);
        adapter.setListener(this);
    }

    private void setupSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener(this); //刷新监听
    }

    @Override
    public void startRefreshing() {
        viewModel.startRefreshing();
    }

    @Override
    public void endRefresh() {
        viewModel.endRefresh();
    }

    /**
     * SwipeRefreshLayout的OnRefresh 这里就需要调用viewModel的一些操作进行数据刷新了
     */
    @Override
    public void onRefresh() {
        viewModel.refreshData();
//        viewModel.
    }

    public Activity getActivityContext() {
        return activityContext;
    }

    public void setActivityContext(Activity activityContext) {
        this.activityContext = activityContext;
    }

    @Override
    public void openGoodsDetail(int goodsId) {
        Intent intent = new Intent(activityContext, GoodsDetailActivity.class);
        intent.putExtra("id", goodsId);
        activityContext.startActivity(intent);
    }

    @Override
    public void refreshUI(String log) {
        getActivityContext().runOnUiThread(() -> {
            ToastUtil.showShort(getContext(), log);
            binding.swipeRefresh.setRefreshing(false);
        });


    }
}
