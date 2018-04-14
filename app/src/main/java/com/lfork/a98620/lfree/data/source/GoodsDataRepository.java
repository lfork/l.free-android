package com.lfork.a98620.lfree.data.source;

import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.source.local.GoodsLocalDataSource;
import com.lfork.a98620.lfree.data.source.remote.GoodsRemoteDataSource;

import java.util.HashMap;
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

    private HashMap<Integer, List<Goods>> goodsMap = new HashMap<>();

    private List<Category> categories;

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
        remoteDataSource.getGoodsList(new GeneralCallback<List<Goods>>() {
            @Override
            public void success(List<Goods> data) {
                List<Goods> localData = goodsMap.get(categoryId);
                if (localData == null){
                    localData = data;
                    goodsMap.put(categoryId, localData);
                } else {
                    localData.clear();
                    localData.addAll(data);
                }
                callback.success(localData);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);

            }
        }, cursor, categoryId);
    }

    @Override
    public void getCategories(GeneralCallback<List<Category>> callback) {
        remoteDataSource.getCategories(new GeneralCallback<List<Category>>() {
            @Override
            public void success(List<Category> data) {
                categories = data;
                callback.success(data);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);

            }
        });
    }


    @Override
    public void refreshData() {
        mCachedDataIsDirty = true;
    }

    @Override
    public void getGoods(GeneralCallback<GoodsDetailInfo> callback, int goodsId) {
        remoteDataSource.getGoods(callback, goodsId);
    }

    @Override
    public void uploadGoods(GeneralCallback<String> callback, Goods g) {
        remoteDataSource.uploadGoods(callback, g);
    }

    public Goods getGoods(int categoryId,int goodsId){
        List<Goods> localData = goodsMap.get(categoryId);

        for(Goods g: localData){
            if (g.getId() == goodsId){
                return g;
            }
        }

        return null;
    }

    public List<Category> getCategories(){
        return categories;
    }
}
