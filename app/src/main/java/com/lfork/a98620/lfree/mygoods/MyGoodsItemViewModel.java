package com.lfork.a98620.lfree.mygoods;

import android.content.Context;

import com.lfork.a98620.lfree.base.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.data.base.entity.Goods;

import java.lang.ref.WeakReference;

/**
 * Created by 98620 on 2018/5/5.
 */
public class MyGoodsItemViewModel extends GoodsViewModel{



    private WeakReference<MyGoodsItemNavigator> reference;

    MyGoodsItemViewModel(Context context, Goods g) {
        super(context, g);

    }



    @Override
    public void onClick(){
        if (reference != null && reference.get() != null)
            reference.get().openGoodsDetail(getId(), getCategoryId());

    }

    @Override
    public void setNavigator(Object navigator) {
        super.setNavigator(navigator);
        this.reference = new WeakReference<>((MyGoodsItemNavigator)navigator);
    }

}
