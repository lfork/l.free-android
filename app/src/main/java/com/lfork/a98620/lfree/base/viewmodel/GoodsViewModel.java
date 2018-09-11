package com.lfork.a98620.lfree.base.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.base.entity.Goods;
import com.lfork.a98620.lfree.base.Config;

/**
 * Created by 98620 on 2018/4/14.
 */

public abstract class GoodsViewModel extends BaseViewModel {


    public ObservableField<String> imagePath = new ObservableField<>(); //封面图片

    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> publishDate = new ObservableField<>();

    public ObservableField<String> price = new ObservableField<>();

    public ObservableField<String> originPrice = new ObservableField<>();

    public ObservableField<String> description = new ObservableField<>();

    public ObservableField<String> type = new ObservableField<>();

    public ObservableArrayList<String> images = new ObservableArrayList<>();


    private int id;

    private int categoryId;

    public GoodsViewModel(Context context, Goods g, int categoryId) {
        this(context, g);
        g.setCategoryId(categoryId);
        this.id = g.getId();
        this.categoryId = categoryId;
    }


    public GoodsViewModel(Context context, Goods g) {
        super(context);
        name.set(g.getName());
        price.set(g.getPrice() + "元");
        imagePath.set(Config.ServerURL + "/image" + g.getCoverImagePath());
        publishDate.set(g.getPublishDate());
        id = g.getId();
        categoryId = g.getCategoryId();
    }

    public GoodsViewModel() {
    }

    public GoodsViewModel(Context context) {
        super(context);
    }


    public void onClick() {
    }

    ;

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
