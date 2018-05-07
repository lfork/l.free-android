package com.lfork.a98620.lfree.base.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.base.BaseViewModel;

/**
 * Created by 98620 on 2018/4/14.
 */

public abstract class GoodsViewModel extends BaseViewModel {

    private int id;

    private int categoryId;

    public GoodsViewModel(Context context, int id, int categoryId) {
        super(context);
        this.id = id;
        this.categoryId = categoryId;
    }

    public GoodsViewModel(Context context, int id) {
        super(context);
        this.id = id;
    }

    public GoodsViewModel() {
    }

    public GoodsViewModel(Context context) {
        super(context);
    }


    public ObservableField<String> imagePath = new ObservableField<>(); //封面图片

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> publishDate = new ObservableField<>();

    public ObservableField<String> price = new ObservableField<>();

    public ObservableField<String> originPrice = new ObservableField<>();

    public ObservableField<String> description = new ObservableField<>();

    public ObservableField<String> type = new ObservableField<>();

    public ObservableArrayList<String> images = new ObservableArrayList<>();


    public void onClick(){};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
