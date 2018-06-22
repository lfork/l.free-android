package com.lfork.a98620.lfree.data.goods;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.entity.Review;

import java.util.List;

/**
 * Created by 98620 on 2018/3/23.
 **/

public interface GoodsDataSource extends DataSource {
    void getGoodsList(GeneralCallback<List<Goods>> callback, String cursor, int categoryId);

    void getUserGoodsList(GeneralCallback<List<Goods>> callback, String cursor, String userId);

    void getCategories(GeneralCallback<List<Category>> callback);

//    void refreshData();  因为这里的数据都直接缓存到view+viewModel里面了。

    void getGoods(GeneralCallback<GoodsDetailInfo> callback, int goodsId);

    void uploadGoods(GeneralCallback<String> callback, Goods g);

    void goodsSearch(GeneralCallback<List<Goods>> callback,  String keyword);

    void deleteGoods(GeneralCallback<String> callback, int goodsId);

    void updateGoods(GeneralCallback<String> callback, Goods g);

    void addReview(GeneralCallback<Review> callback, Review review);
}
