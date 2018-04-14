package com.lfork.a98620.lfree.common;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;

/**
 * Created by 98620 on 2018/3/19.
 */

public abstract class BaseViewModel extends BaseObservable{

    public final ObservableBoolean dataIsLoading = new ObservableBoolean(true);

    public Context context;

    public BaseViewModel() {
    }

    public BaseViewModel(Context context) {
        this.context = context;
    }


}
