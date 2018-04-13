package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.common.BaseViewModel;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.goodsdetail.GoodsDetailActivity;
import com.lfork.a98620.lfree.util.Config;

import java.io.ObjectStreamField;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class GoodsItemViewModel extends BaseViewModel{

    private Context context;

    public String name;

    public String publishDate;

    private int id;

    private int categoryId;

    public String price;

    public ObservableField<String> imagePath = new ObservableField<>();

    GoodsItemViewModel(Context context, Goods g, int categoryId) {
        this.categoryId = categoryId;
        this.context = context;
        name = g.getName();
        id = g.getId();
        price = g.getPrice() + "元";
        imagePath.set(Config.ServerURL + "/image" + g.getCoverImagePath() ); //
        publishDate = g.getPublishDate();
    }


    public void onClick(){
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("category_id", categoryId);
        context.startActivity(intent);
    }
}
