package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.content.Intent;

import com.lfork.a98620.lfree.common.BaseViewModel;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.goodsdetail.GoodsDetailActivity;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class GoodsItemViewModel extends BaseViewModel{

    private Context context;

    public String name;

    public String publishDate;

    private int id;

    public String price;

    public String imagePath;

    public GoodsItemViewModel(Context context, Goods g) {
        this.context = context;
        name = g.getName();
        id = g.getId();
        price = g.getPrice();
        imagePath = g.getCoverImagePath();
        publishDate = g.getPublishDate();
        imagePath = g.getCoverImagePath();
    }


    public void onClick(){
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        context.startActivity(intent);
    }
}
