package com.lfork.a98620.lfree.data.source;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.source.local.GoodsLocalDataSource;
import com.lfork.a98620.lfree.data.source.remote.GoodsRemoteDataSource;

import java.util.List;

/**
 * 
 * Created by 98620 on 2018/4/7.
 */

public class GoodsDataRepository implements GoodsDataSource {
    private static GoodsDataRepository INSTANCE;
//    private ArrayList<ResponseGetGoods.GoodsInfo> mCachedGoodsList = new ArrayList<>();

    private static final String TAG = "GoodsDataRepository ";

    private GoodsRemoteDataSource remoteDataSource;

    private GoodsLocalDataSource localDataSource;

    private boolean mCachedDataIsDirty = true;

    private Goods mGoods;

    private GoodsDataRepository(GoodsRemoteDataSource remoteDataSource, GoodsLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }


    public static GoodsDataRepository getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new GoodsDataRepository(GoodsRemoteDataSource.getInstance(), GoodsLocalDataSource.getInstance());
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getGoodsList(GeneralCallback<List<Goods>> callback, String cursor, int categoryId) {
        remoteDataSource.getGoodsList(callback, cursor,categoryId);
    }

    @Override
    public void getCategories(GeneralCallback<List<Category>> callback) {
        remoteDataSource.getCategories(callback);
    }


    @Override
    public void refreshData() {
        mCachedDataIsDirty = true;
    }
}
