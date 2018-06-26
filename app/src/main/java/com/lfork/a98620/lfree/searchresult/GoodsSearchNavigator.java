package com.lfork.a98620.lfree.searchresult;

import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;

/**
 * Created by 98620 on 2018/6/26.
 */
public interface GoodsSearchNavigator extends ViewModelNavigator{
    void openGoodsDetail(int goodsId,int categoryId);

    void cancelSearch();
}
