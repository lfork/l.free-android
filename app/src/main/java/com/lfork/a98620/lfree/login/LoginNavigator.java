package com.lfork.a98620.lfree.login;

import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;

/**
 *
 * @author 98620
 * @date 2018/7/11
 */
public interface LoginNavigator extends ViewModelNavigator {
    void loginSucceed();

    void loginFailed(String log);

    void openRegisterAct();

    void openForgetPasswordAct();
}
