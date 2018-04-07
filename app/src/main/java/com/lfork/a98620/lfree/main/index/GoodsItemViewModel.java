package com.lfork.a98620.lfree.main.index;

import android.databinding.ObservableField;
import android.view.View;

import com.lfork.a98620.lfree.BaseViewModel;
import com.lfork.a98620.lfree.data.entity.Goods;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class GoodsItemViewModel extends BaseViewModel{

    public String name;

    public final ObservableField<String> userPortraitPath = new ObservableField<>();

    public String publishDate;

    private int id;

    public String price;

    public String imagePath;

    public GoodsItemViewModel(Goods g) {
        name = g.getName();
        id = g.getId();
        price = g.getPrice();
        imagePath = g.getImagePath();
        publishDate = g.getPublishDate();
        imagePath = g.getImagePath();
        userPortraitPath.set(g.getUserPortraitPath());
    }
}
