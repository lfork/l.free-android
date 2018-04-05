package com.lfork.a98620.lfree;

import android.content.Context;
import android.databinding.BaseObservable;

/**
 * Created by 98620 on 2018/3/19.
 */

public class BaseViewModel extends BaseObservable{
    private Context context;

    public BaseViewModel() {
    }

    public BaseViewModel(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
