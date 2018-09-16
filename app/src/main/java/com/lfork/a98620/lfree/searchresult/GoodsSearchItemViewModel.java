package com.lfork.a98620.lfree.searchresult;

import android.content.Context;

import com.lfork.a98620.lfree.base.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.data.base.entity.Goods;

import java.lang.ref.WeakReference;

/**
 * Created by 98620 on 2018/4/14.
 */

public  class GoodsSearchItemViewModel extends GoodsViewModel {

    private WeakReference<GoodsSearchNavigator> reference;

    public GoodsSearchItemViewModel(Context context, Goods g) {
        super(context, g);
    }

    public void setNavigator(GoodsSearchNavigator navigator) {
        super.setNavigator(navigator);
        reference = new WeakReference<>(navigator);
    }

    public void onClick(){
        if (reference != null && reference.get() != null) {
            reference.get().openGoodsDetail(getId(), getCategoryId());
        }
    }
}
