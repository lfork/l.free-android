package com.lfork.a98620.lfree.mygoods;

import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;

public interface MyGoodsItemNavigator extends ViewModelNavigator {
    void openGoodsDetail(int goodsId, int categoryId);
}
