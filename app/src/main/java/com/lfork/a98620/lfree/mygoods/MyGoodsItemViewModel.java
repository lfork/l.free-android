package com.lfork.a98620.lfree.mygoods;

import android.content.Context;

import com.lfork.a98620.lfree.base.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.goodsdetail.GoodsDetailActivity;

import java.lang.ref.WeakReference;

/**
 * Created by 98620 on 2018/5/5.
 */
public class MyGoodsItemViewModel extends GoodsViewModel{



    private WeakReference<MyGoodsActivityNavigator> reference;

    MyGoodsItemViewModel(Context context, Goods g) {
        super(context, g);

    }



    @Override
    public void onClick(){
        if (reference != null && reference.get() != null)
            reference.get().openGoodsDetail(getId(), getCategoryId());

    }

    public void setNavigator(MyGoodsActivityNavigator navigator) {
        super.setNavigator(navigator);
        this.reference = new WeakReference<>(navigator);
    }

}
