package com.lfork.a98620.lfree.base.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.base.BaseViewModel;

/**
 * Created by 98620 on 2018/4/14.
 */

public abstract class GoodsViewModel extends BaseViewModel {

    public GoodsViewModel() {
        super();
    }

    public GoodsViewModel(Context context) {
        super(context);
    }

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> publishDate = new ObservableField<>();

    public ObservableField<String> price = new ObservableField<>();

    public ObservableField<String> originPrice = new ObservableField<>();

    public ObservableField<String> description = new ObservableField<>();

    public ObservableField<String> type = new ObservableField<>();

    public ObservableArrayList<String> images = new ObservableArrayList<>();
}
