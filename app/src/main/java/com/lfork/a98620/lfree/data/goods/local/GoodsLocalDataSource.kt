package com.lfork.a98620.lfree.data.goods.local

import com.lfork.a98620.lfree.data.DataSource
import com.lfork.a98620.lfree.data.base.entity.Category
import com.lfork.a98620.lfree.data.base.entity.Goods
import com.lfork.a98620.lfree.data.base.entity.GoodsDetailInfo
import com.lfork.a98620.lfree.data.base.entity.Review
import com.lfork.a98620.lfree.data.goods.GoodsDataSource
import org.litepal.crud.DataSupport

/**
 *
 * Created by 98620 on 2018/9/2.
 */
object GoodsLocalDataSource : GoodsDataSource {
    override fun getGoodsList(callback: DataSource.GeneralCallback<List<Goods>>, cursor: String, categoryId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserGoodsList(callback: DataSource.GeneralCallback<List<Goods>>, cursor: String, userId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGoods(callback: DataSource.GeneralCallback<GoodsDetailInfo>, goodsId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteGoods(callback: DataSource.GeneralCallback<String>, goodsId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun uploadGoods(callback: DataSource.GeneralCallback<String>, g: Goods) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goodsSearch(callback: DataSource.GeneralCallback<List<Goods>>, keyword: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateGoods(callback: DataSource.GeneralCallback<String>, g: Goods) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addReview(callback: DataSource.GeneralCallback<Review>, review: Review) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun destroyInstance() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCategories(callback: DataSource.GeneralCallback<List<Category>>) {
        val categories = DataSupport.findAll(Category::class.java)
        if (categories != null) {
            callback.succeed(categories)
        } else {
            callback.failed("本地数据为空")
        }
    }
}
