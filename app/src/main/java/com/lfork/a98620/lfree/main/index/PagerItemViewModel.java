package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.Category;
import com.lfork.a98620.lfree.data.base.entity.Goods;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.util.TimeUtil;

import java.util.List;

/**
 * Created by 98620 on 2018/3/31.
 */
public class PagerItemViewModel extends BaseViewModel implements PagerDataRefreshListener {

    public final ObservableArrayList<GoodsItemViewModel> items = new ObservableArrayList<>();

    public ObservableBoolean isLoadingMoreData = new ObservableBoolean(false);

    /**
     * 数据是否滑动到最下面的标志
     */
    public ObservableBoolean isDown = new ObservableBoolean(false);

    private String tempLog;

    private PagerItemViewNavigator navigator;

    private boolean isRefreshed = true;

    private Category category;
    private boolean isInitialized = false;
    private GoodsDataRepository repository;

    private final static int INITIALIZE = 0;
    private final static int LOAD_MORE = 1;
    private final static int REFRESH = 2;

    PagerItemViewModel(Context context, Category category) {
        super(context);
        this.category = category;
        repository = GoodsDataRepository.INSTANCE;
    }

    /**
     * 刷新ui
     */
    private void refreshUI(String log) {
        dataIsLoading.set(false);
        if (navigator != null) {
          //  navigator.toast(log);
            isRefreshed = true;
        } else {
            tempLog = log;
        }
    }

    /**
     * 第一次加载数据，初始化数据 ，当选中这个tab时进行数据的初始化操作
     */
    private void initData() {
        if (isInitialized) {
            //说明还没有完成刷新
            if (!isRefreshed && !dataIsLoading.get()) {
                refreshUI(tempLog);
            }
        } else {
            isInitialized = true;
            getGoodsList(INITIALIZE, TimeUtil.getBiggestTime());//DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString()  保证数据的绝对最新
        }
    }

    //上拉加载更多
    private void loadMoreData() {
        String cursor = items.get(items.size() - 1).publishDate.get();
        getGoodsList(LOAD_MORE, cursor);
    }

    //下拉刷新
    public void refreshData() {
        isRefreshed = false;
        getGoodsList(REFRESH,TimeUtil.getBiggestTime() );//DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString()  保证数据的绝对最新
    }

    private void getGoodsList(int requestType, String... cursor) {
        if (cursor != null)
            new Thread(() -> {
                if (requestType != LOAD_MORE) {
                    //强行等0.5秒，这样可以优化加载效果。缓冲一下
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                repository.getGoodsList(new DataSource.GeneralCallback<List<Goods>>() {
                    @Override
                    public void succeed(List<Goods> data) {

                        switch (requestType) {
                            case INITIALIZE:
                            case REFRESH:
                                items.clear();
                                break;
                            case LOAD_MORE:
                                break;
                            default:
                                break;
                        }


                        for (Goods g: data) {
                            GoodsItemViewModel viewModel = new GoodsItemViewModel(getContext(), g);
                            items.add(viewModel);
                        }

                        switch (requestType) {
                            case INITIALIZE:
                                refreshUI("数据初始化成功");
                                break;
                            case REFRESH:
                                refreshUI("数据刷新成功");
                                navigator.refreshEnd();
                                break;
                            case LOAD_MORE:
                                refreshUI("数据加载成功");
                                endRefresh();
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void failed(String log) {
                        refreshUI(log);
                    }
                }, cursor[0], category.getCsId());
            }).start();
    }

    @Override
    public void startRefreshing() {
        if (!isLoadingMoreData.get()) {
            isLoadingMoreData.set(true);
            loadMoreData();
        }
    }

    @Override
    public void endRefresh() {
        isLoadingMoreData.set(false);
    }

    @Override
    public void onDown() {
        isDown.set(true);
    }

    /**
     * 除了有自己获取数据的 start 还有 setData? {@link # s etData()}  setData是给被动viewModel使用的
     * 开始获取数据
     */
    @Override
    public void start() {
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigator = null;
    }

    public void setNavigator(PagerItemViewNavigator navigator) {
        this.navigator = navigator;
    }

    public boolean isInitialized() {
        return isInitialized;
    }


    public Category getCategory() {
        return category;
    }
}
