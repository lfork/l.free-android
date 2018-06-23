package com.lfork.a98620.lfree.base.viewmodel;

/**
 * Created by 98620 on 2018/4/25.
 *
 * navigator
 */
public interface ViewModelNavigator {

    //大部分ViewModel 应该都有一个Toast的操作：不管是测试也好还是提示用户也行
    //I consider that most viewModels all have a need to send a Toast, for testing or giving tips to user
    void showToast(String msg);

}
