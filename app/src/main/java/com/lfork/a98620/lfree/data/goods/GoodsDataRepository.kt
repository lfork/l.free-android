package com.lfork.a98620.lfree.data.goods

import android.util.SparseArray
import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.base.entity.Category
import com.lfork.a98620.lfree.data.base.entity.Goods
import com.lfork.a98620.lfree.data.base.entity.GoodsDetailInfo
import com.lfork.a98620.lfree.data.base.entity.Review
import com.lfork.a98620.lfree.data.goods.local.GoodsLocalDataSource
import com.lfork.a98620.lfree.data.goods.remote.GoodsRemoteDataSource

/**
 *
 * Created by 98620 on 2018/9/2.
 */
object GoodsDataRepository : GoodsDataSource {

    private var remoteDataSource = GoodsRemoteDataSource

    private var localDataSource = GoodsLocalDataSource

    /**
     * 满足下面两个条件我们可以使用SparseArray代替HashMap：
     * 数据量不大，最好在千级以内
     * key必须为int类型，这中情况下的HashMap可以用SparseArray代替：
     *
     * //因为商品数据 的及时性要求是较高的，所以就不做本地缓存了和仓库内存缓存了，而是直接把数据放到view里面进行缓存
     * //更新的话 也是直接请求网络数据进行更新
     * //然后商品种类的 倒是可以做一下内存缓存，因为上传商品的时候和显示商品详情的时候都会用到商品种类数据。
     */
    private val goodsMap = SparseArray<List<Goods>>()

    private var categories: List<Category>? = null


    fun destroyInstance() {
        remoteDataSource.destroyInstance()
        localDataSource.destroyInstance()
        goodsMap.clear()
        categories = null
    }

    override fun getGoodsList(callback: GeneralCallback<List<Goods>>, cursor: String, categoryId: Int) {
        remoteDataSource.getGoodsList(object : GeneralCallback<List<Goods>> {
            override fun succeed(data: List<Goods>) {
                var localData: MutableList<Goods>? = goodsMap.get(categoryId) as MutableList<Goods>?
                if (localData == null) {
                    localData = data as MutableList<Goods>?
                    goodsMap.put(categoryId, localData)
                } else {
                    localData.clear()
                    localData.addAll(data)
                }
                callback.succeed(localData as List<Goods>)
            }

            override fun failed(log: String) {
                callback.failed(log)

            }
        }, cursor, categoryId)
    }

    override fun getUserGoodsList(callback: GeneralCallback<List<Goods>>, cursor: String, userId: String) {
        remoteDataSource.getUserGoodsList(callback, cursor, userId)
    }

    override fun getCategories(callback: GeneralCallback<List<Category>>) {

        if (categories != null && categories!!.size > 0) {
            callback.succeed(categories!!)
            return
        }
        remoteDataSource.getCategories(object : GeneralCallback<List<Category>> {
            override fun succeed(data: List<Category>) {
                categories = data
                callback.succeed(data)
                //                //做本地储存
                //                DataSupport.deleteAll(Category.class);
                //                DataSupport.saveAllAsync(categories).listen(success -> Log.d(TAG, "onFinish: 商品分类储存成功"));
            }

            override fun failed(log: String) {
                callback.failed(log)
            }
        })
    }


    override fun getGoods(callback: GeneralCallback<GoodsDetailInfo>, goodsId: Int) {
        remoteDataSource.getGoods(callback, goodsId)
    }

    override fun uploadGoods(callback: GeneralCallback<String>, g: Goods) {
        remoteDataSource.uploadGoods(callback, g)
    }

    override fun goodsSearch(callback: GeneralCallback<List<Goods>>, keyword: String) {
        remoteDataSource.goodsSearch(callback, keyword)
    }

    override fun deleteGoods(callback: GeneralCallback<String>, goodsId: Int) {
        remoteDataSource.deleteGoods(callback, goodsId)
    }

    override fun updateGoods(callback: GeneralCallback<String>, g: Goods) {
        remoteDataSource.updateGoods(callback, g)
    }

    override fun addReview(callback: GeneralCallback<Review>, review: Review) {
        remoteDataSource.addReview(callback, review)

    }

}
