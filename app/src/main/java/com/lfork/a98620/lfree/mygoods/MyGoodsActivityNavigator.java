package com.lfork.a98620.lfree.mygoods;

import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;

/**
 * Created by 98620 on 2018/5/5.
 */
interface MyGoodsActivityNavigator extends ViewModelNavigator {
    void refreshFinished(String log);

    void loadMoreFinished(String log);

}
