package com.lfork.a98620.lfree.main.myinfo;

import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;

/**
 * Created by 98620 on 2018/6/23.
 */
public interface MyInfoFragmentNavigator extends ViewModelNavigator {
    void openMyGoods();

    void openSettings();

    void openUserInfoDetail();

    void logoff();
}
