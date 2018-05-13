package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.content.Intent;

import com.lfork.a98620.lfree.base.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.goodsdetail.GoodsDetailActivity;
import com.lfork.a98620.lfree.util.Config;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class GoodsItemViewModel extends GoodsViewModel{

    GoodsItemViewModel(Context context, Goods g, int categoryId) {
        super(context, g.getId(), categoryId);
        this.context = context;
        name.set(g.getName());
        price.set(g.getPrice() + "元");
        imagePath.set(Config.ServerURL + "/image" + g.getCoverImagePath() );
        publishDate.set(g.getPublishDate());
    }


    public void onClick(){

        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("id", getId());
        intent.putExtra("category_id", getCategoryId());
        context.startActivity(intent);
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }
}
