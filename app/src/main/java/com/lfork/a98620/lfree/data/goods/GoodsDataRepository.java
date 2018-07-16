package com.lfork.a98620.lfree.data.goods;

import android.util.SparseArray;

import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.entity.Review;
import com.lfork.a98620.lfree.data.goods.local.GoodsLocalDataSource;
import com.lfork.a98620.lfree.data.goods.remote.GoodsRemoteDataSource;

import java.util.List;

/**
 * 
 * @author 98620
 * @date 2018/4/7
 */
public class GoodsDataRepository implements GoodsDataSource {
    private static GoodsDataRepository INSTANCE;
//    private ArrayList<ResponseGetGoods.GoodsInfo> mCachedGoodsList = new ArrayList<>();

    private static final String TAG = "GoodsDataRepository ";

    private GoodsRemoteDataSource remoteDataSource;

    private GoodsLocalDataSource localDataSource;

    /**
     * 满足下面两个条件我们可以使用SparseArray代替HashMap：
     *  数据量不大，最好在千级以内
     *  key必须为int类型，这中情况下的HashMap可以用SparseArray代替：
     *
     *  //因为商品数据 的及时性要求是较高的，所以就不做本地缓存了和仓库内存缓存了，而是直接把数据放到view里面进行缓存
     *  //更新的话 也是直接请求网络数据进行更新
     *  //然后商品种类的 倒是可以做一下内存缓存，因为上传商品的时候和显示商品详情的时候都会用到商品种类数据。
     */
    private SparseArray<List<Goods>> goodsMap = new SparseArray<>();

    private List<Category> categories;

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

        if (categories != null && categories.size() > 0) {
            callback.succeed(categories);
            return;
        }
        remoteDataSource.getCategories(new GeneralCallback<List<Category>>() {
            @Override
            public void succeed(List<Category> data) {
                categories = data;
                callback.succeed(data);
//                //做本地储存
//                DataSupport.deleteAll(Category.class);
//                DataSupport.saveAllAsync(categories).listen(success -> Log.d(TAG, "onFinish: 商品分类储存成功"));
            }

            @Override
            public void failed(String log) {
                callback.failed(log);
            }
        });
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
    public void deleteGoods(GeneralCallback<String> callback, int goodsId) {
        remoteDataSource.deleteGoods(callback, goodsId);
    }

    @Override
    public void updateGoods(GeneralCallback<String> callback, Goods g) {
        remoteDataSource.updateGoods(callback,g);
    }

    @Override
    public void addReview(GeneralCallback<Review> callback, Review review) {
        remoteDataSource.addReview(callback,  review);

    }
}
