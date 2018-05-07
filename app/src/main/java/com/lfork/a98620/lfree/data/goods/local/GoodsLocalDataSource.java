package com.lfork.a98620.lfree.data.goods.local;

import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.goods.GoodsDataSource;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 98620 on 2018/3/23.
 */

public class GoodsLocalDataSource implements GoodsDataSource {

    private static GoodsLocalDataSource INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "IMRemoteDataSource";

    private GoodsLocalDataSource() {
    }

    public static GoodsLocalDataSource getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new GoodsLocalDataSource();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getGoodsList(GeneralCallback<List<Goods>> callback, String cursor, int categoryId) {

    }

    @Override
    public void getUserGoodsList(GeneralCallback<List<Goods>> callback, String cursor, String userId) {

    }

    @Override
    public void getCategories(GeneralCallback<List<Category>> callback) {
        List<Category> categories = DataSupport.findAll(Category.class);

        if (categories != null) {
            callback.succeed(categories);
        } else {
            callback.failed("网络连接失败");
        }
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void getGoods(GeneralCallback<GoodsDetailInfo> callback, int goodsId) {

    }

    @Override
    public void uploadGoods(GeneralCallback<String> callback, Goods g) {

    }

    @Override
    public void goodsSearch(GeneralCallback<List<Goods>> callback, String keyword) {

    }
}
