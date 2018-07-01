package com.lfork.a98620.lfree.main.index;

/**
 * Created by 98620 on 2018/5/5.
 */
public interface PagerDataRefreshListener {
    void startRefreshing();

    void endRefresh();

    /**
     * 数据滑动到最下面了
     */
    void onDown();


}
