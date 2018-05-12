package com.lfork.a98620.lfree.goodsdetail;

import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;

/**
 * Created by 98620 on 2018/5/9.
 */
public interface GoodsDetailNavigator extends ViewModelNavigator{

    @Override
    void notifyDataChanged();

    void notifyReviewChanged();

    @Override
    void setParam1(String param);

    @Override
    void setParam2(String param);
}
