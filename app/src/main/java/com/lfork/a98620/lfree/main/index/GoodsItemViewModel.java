package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.support.annotation.Nullable;

import com.lfork.a98620.lfree.base.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.data.base.entity.Goods;

import java.lang.ref.WeakReference;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class GoodsItemViewModel extends GoodsViewModel{

    // This navigator is s wrapped in a WeakReference to avoid leaks because it has references to an
    // activity. There's no straightforward way to clear it for each item in a list adapter.

    @Nullable
    private WeakReference<GoodsItemNavigator> mNavigator;

    private GoodsItemViewModel(Context context, Goods g, int categoryId) {
        super(context, g, categoryId);
    }

    GoodsItemViewModel(Context context, Goods g) {
        this(context, g, 0);
    }


    public void onClick(){
        int goodsId = getId();
        if (goodsId == 0) {
            // Click happened before goods was loaded, no-op.
            return;
        }
        if (mNavigator != null && mNavigator.get() != null) {
            mNavigator.get().openGoodsDetail(goodsId, getCategoryId());
        }
    }

    public void setNavigator(GoodsItemNavigator mNavigator) {
        this.mNavigator = new WeakReference<>(mNavigator);
    }
}
