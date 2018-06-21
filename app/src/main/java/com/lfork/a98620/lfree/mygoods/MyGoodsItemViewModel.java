package com.lfork.a98620.lfree.mygoods;

import android.content.Context;

import com.lfork.a98620.lfree.base.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.goodsdetail.GoodsDetailActivity;
import com.lfork.a98620.lfree.util.Config;

import java.lang.ref.WeakReference;

/**
 * Created by 98620 on 2018/5/5.
 */
public class MyGoodsItemViewModel extends GoodsViewModel{


    private WeakReference<Context> reference;

    MyGoodsItemViewModel(Context context, Goods g) {
        super(context, g.getId());
        reference = new WeakReference<>(context);
        name.set(g.getName());
        price.set(g.getPrice() + "元");
        imagePath.set(Config.ServerURL + "/image" + g.getCoverImagePath() ); //
        publishDate.set(g.getPublishDate());
    }

    public void onClick(){
        if (reference != null && reference.get() != null)
        GoodsDetailActivity.startActivity(reference.get(), getId(), getCategoryId());
    }

}
