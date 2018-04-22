package com.lfork.a98620.lfree.main.index;

import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateFormat;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.common.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.source.GoodsDataRepository;
import com.lfork.a98620.lfree.databinding.MainIndexViewpagerItemBinding;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 98620 on 2018/3/31.
 */

public class IndexPagerItemViewModel extends BaseViewModel {
    private static final String TAG = "IndexPagerItemViewModel";

    private Category category;
    //    MainIndexVBinding binding;
    private MainIndexViewpagerItemBinding binding;
    private List<Goods> goodsList;
    private List<GoodsItemViewModel> models = new ArrayList<>();
    private FragmentActivity context;
    private GoodsRecyclerViewItemAdapter adapter;
    private boolean isInitialized = false;
    private GoodsDataRepository repository;

    private final static int INITIALIZE = 0;
    private final static int LOAD_MORE = 1;
    private final static int REFRESH = 2;


    IndexPagerItemViewModel(Category category, ViewDataBinding binding, FragmentActivity context) {
        super(context);
        this.category = category;
        this.binding = (MainIndexViewpagerItemBinding) binding;
        this.context = context;
        repository = GoodsDataRepository.getInstance();
        initUI();
    }


    private void initUI() {
        RecyclerView recyclerView = binding.mainIndexRecycle;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GoodsRecyclerViewItemAdapter<>(models, R.layout.goods_recycle_item);
        recyclerView.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });  //刷新监听
    }

    /**
     * 刷新ui
     */
    private void refreshUI(String log) {
        ToastUtil.showShort(context, log);
        binding.swipeRefresh.setRefreshing(false);
        adapter.notifyDataSetChanged();
        dataIsLoading.set(false);
    }

    /**
     * 第一次加载数据，初始化数据 ，当选中这个tab时进行数据的初始化操作
     */
    void initData() {
        if (isInitialized) {
            return;
        }
        isInitialized = true;
        getGoodsList(INITIALIZE, DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString());
    }

    //上拉加载更多
    private void loadMoreData() {
        getGoodsList(LOAD_MORE, DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString());

    }


    //下拉刷新
    private void refreshData() {
        getGoodsList(REFRESH, DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString());

    }

    private void getGoodsList(int requestType, String... cursor) {
        if (cursor != null)
            new Thread(() -> {
                //强行等一秒，这样可以优化加载效果。缓冲一下
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                repository.getGoodsList(new DataSource.GeneralCallback<List<Goods>>() {
                    @Override
                    public void succeed(List<Goods> data) {
                        goodsList = data;
                        models.clear();
                        for (Goods g : goodsList) {
                            models.add(new GoodsItemViewModel(context, g, category.getCsId()));
                        }
                        context.runOnUiThread(() -> {
                            switch (requestType) {
                                case INITIALIZE:
                                    refreshUI("数据初始化成功");
                                    break;
                                case REFRESH:
                                    refreshUI("数据刷新成功");
                                    break;
                                case LOAD_MORE:
                                    refreshUI("数据加载成功");
                                    break;
                                default:
                                    break;
                            }
                        });
                    }

                    @Override
                    public void failed(String log) {
                        context.runOnUiThread(() -> {
                            refreshUI(log);
                        });
                    }
                }, cursor[0], category.getCsId());
            }).start();

    }

}
