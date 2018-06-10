package com.lfork.a98620.lfree.data.goods;

import android.util.Log;

import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.entity.Review;
import com.lfork.a98620.lfree.data.goods.local.GoodsLocalDataSource;
import com.lfork.a98620.lfree.data.goods.remote.GoodsRemoteDataSource;

import org.litepal.crud.DataSupport;

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
            public void succeed(List<Goods> data) {
                List<Goods> localData = goodsMap.get(categoryId);
                if (localData == null){
                    localData = data;
                    goodsMap.put(categoryId, localData);
                } else {
                    localData.clear();
                    localData.addAll(data);
                }
                callback.succeed(localData);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);

            }
        }, cursor, categoryId);
    }

    @Override
    public void getUserGoodsList(GeneralCallback<List<Goods>> callback, String cursor, String userId) {
        remoteDataSource.getUserGoodsList(callback, cursor, userId);
    }

    @Override
    public void getCategories(GeneralCallback<List<Category>> callback) {
        remoteDataSource.getCategories(new GeneralCallback<List<Category>>() {
            @Override
            public void succeed(List<Category> data) {
                categories = data;
                callback.succeed(data);
                //做本地储存
                DataSupport.deleteAll(Category.class);
                DataSupport.saveAllAsync(categories).listen(success -> Log.d(TAG, "onFinish: 商品分类储存成功"));
            }

            @Override
            public void failed(String log) {
//                callback.failed(log);
                //获取本地储存的数据
                getLocalCategoryData(callback, log);
            }
        });
    }


    private void getLocalCategoryData(GeneralCallback<List<Category>> callback, String log0){
        localDataSource.getCategories(new GeneralCallback<List<Category>>() {
            @Override
            public void succeed(List<Category> data) {
                categories = data;
                callback.succeed(data);
            }

            @Override
            public void failed(String log) {
                callback.failed(log0);

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

    @Override
    public void goodsSearch(GeneralCallback<List<Goods>> callback, String keyword) {
        remoteDataSource.goodsSearch(callback, keyword);
    }

    @Override
    public void addReview(GeneralCallback<Review> callback, Review review) {
        remoteDataSource.addReview(callback,  review);

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
