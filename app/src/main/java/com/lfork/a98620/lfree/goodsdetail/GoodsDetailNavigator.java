package com.lfork.a98620.lfree.goodsdetail;

import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;

import java.util.ArrayList;

/**
 * Created by 98620 on 2018/5/9.
 */
public interface GoodsDetailNavigator extends ViewModelNavigator{

    void notifyReviewChanged();

    void closeSoftKeyBoard();

    void openUserInfo(int userId);

    void openChatWindow(String s, int userId);

    void openBigImages();

    void refreshBanner(ArrayList<String> images);

}
