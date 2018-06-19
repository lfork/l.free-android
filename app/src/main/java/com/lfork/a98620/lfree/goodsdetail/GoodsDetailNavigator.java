package com.lfork.a98620.lfree.goodsdetail;

import android.databinding.ObservableArrayList;

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

    void openBigImages(ObservableArrayList<String> images, int position);

    void refreshBanner(ArrayList<String> images);

    void setActionBar();

}
