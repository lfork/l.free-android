package com.lfork.a98620.lfree.data.goods

import com.lfork.a98620.lfree.data.DataSource
import com.lfork.a98620.lfree.data.base.entity.Category
import com.lfork.a98620.lfree.data.base.entity.Goods
import com.lfork.a98620.lfree.data.base.entity.GoodsDetailInfo
import com.lfork.a98620.lfree.data.base.entity.Review

/**
 *
 * Created by 98620 on 2018/9/2.
 */
interface GoodsDataSource : DataSource {
    fun getGoodsList(callback: DataSource.GeneralCallback<List<Goods>>, cursor: String, categoryId: Int)

    fun getUserGoodsList(callback: DataSource.GeneralCallback<List<Goods>>, cursor: String, userId: String)

    fun getCategories(callback: DataSource.GeneralCallback<List<Category>>)

    fun getGoods(callback: DataSource.GeneralCallback<GoodsDetailInfo>, goodsId: Int)

    fun uploadGoods(callback: DataSource.GeneralCallback<String>, g: Goods)

    fun goodsSearch(callback: DataSource.GeneralCallback<List<Goods>>, keyword: String)

    fun deleteGoods(callback: DataSource.GeneralCallback<String>, goodsId: Int)

    fun updateGoods(callback: DataSource.GeneralCallback<String>, g: Goods)

    fun addReview(callback: DataSource.GeneralCallback<Review>, review: Review)
}