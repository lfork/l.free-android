package com.lfork.a98620.lfree.main.index;

import android.databinding.ObservableField;

import com.lfork.a98620.lfree.BaseViewModel;
import com.lfork.a98620.lfree.data.entity.Goods;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class GoodsItemViewModel extends BaseViewModel{

    public String name;

    public String publishDate;

    private int id;

    public String price;

    public String imagePath;

    public GoodsItemViewModel(Goods g) {
        name = g.getName();
        id = g.getId();
        price = g.getPrice();
        imagePath = g.getCoverImagePath();
        publishDate = g.getPublishDate();
        imagePath = g.getCoverImagePath();
    }
}
