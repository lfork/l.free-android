package com.lfork.a98620.lfree.base;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.support.annotation.CallSuper;

import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;

/**
 * Created by 98620 on 2018/3/19.
 */

public abstract class BaseViewModel extends BaseObservable {

    public final ObservableBoolean dataIsLoading = new ObservableBoolean(true);

    public final ObservableBoolean dataIsEmpty = new ObservableBoolean(false);

    private Context context;

    private ViewModelNavigator navigator;

    public void start() {
    }

    /**
     * 释放掉一些东西，防止内存泄漏
     */
    @CallSuper
    public void onDestroy() {
        navigator = null;
    }

    public BaseViewModel() {
    }

    public BaseViewModel(Context context) {
        this.context = context.getApplicationContext();     //force to use application context, avoid potential memory leak
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     *
     * @param navigator 自定义的navigator 必须是继承自{@link ViewModelNavigator}
     *                  否则下面会报错
     *                  继承自{@link ViewModelNavigator}  只有一个作用 ，通用的toast操作
     */
    public void setNavigator(Object navigator){
        this.navigator = (ViewModelNavigator) navigator;
    }

    //    public abstract void showToast();     大部分viewModel应该都有提示的操作：Toast,SnackBar,自定义提示等等
    public void showToast(String msg) {
        if (navigator != null) {
            navigator.showToast(msg);
        }
    }
}
