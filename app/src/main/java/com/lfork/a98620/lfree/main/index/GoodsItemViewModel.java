package com.lfork.a98620.lfree.main.index;

import android.view.View;

import com.lfork.a98620.lfree.BaseViewModel;
import com.lfork.a98620.lfree.data.entity.Goods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 98620 on 2018/3/31.
 */

public class GoodsItemViewModel extends BaseViewModel{

    public String name;

    public String description;

    private int id;

    public String price;

    private String imagePath;

    public GoodsItemViewModel(Goods g) {
        name = g.getName();
        id = g.getId();
        price = g.getPrice();
        imagePath = g.getImagePath();
        description = g.getDescription();

    }
}
