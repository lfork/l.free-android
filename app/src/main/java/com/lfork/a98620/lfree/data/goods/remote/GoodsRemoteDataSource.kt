package com.lfork.a98620.lfree.data.goods.remote

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.lfork.a98620.lfree.base.Config
import com.lfork.a98620.lfree.base.network.HttpService
import com.lfork.a98620.lfree.base.network.MyRetrofitCallBack
import com.lfork.a98620.lfree.base.network.Result
import com.lfork.a98620.lfree.data.base.api.GoodsApi
import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.base.entity.*
import com.lfork.a98620.lfree.data.goods.GoodsDataSource
import com.lfork.a98620.lfree.util.JSONUtil
import okhttp3.*
import java.io.File

/**
 *
 * Created by 98620 on 2018/9/2.
 */

object GoodsRemoteDataSource : GoodsDataSource {
    fun destroyInstance() {
        //Do nothing here temporarily
    }

    private val TAG = "IMRemoteDataSource"

    private val api = GoodsApi.create()

    override fun getGoodsList(callback: GeneralCallback<List<Goods>>, cursor: String, categoryId: Int) {
        api.getGoodsList(categoryId, cursor).enqueue(MyRetrofitCallBack(callback));
    }

    override fun getUserGoodsList(callback: GeneralCallback<List<Goods>>, cursor: String, userId: String) {
        api.getUserGoodsList(userId, cursor).enqueue(MyRetrofitCallBack(callback))
    }

    override fun getCategories(callback: GeneralCallback<List<Category>>) {
        api.getUserGoodsList().enqueue(MyRetrofitCallBack(callback))
    }

    override fun getGoods(callback: GeneralCallback<GoodsDetailInfo>, goodsId: Int) {
        api.getGoods(goodsId.toString()).enqueue(MyRetrofitCallBack(callback))
    }

    override fun uploadGoods(callback: GeneralCallback<String>, g: Goods) {
        val images = g.imagesPath
        Log.d(TAG, "uploadGoods: " + g.publishDate)

        val files = arrayOfNulls<RequestBody>(images!!.size)

        for (i in images.indices) {
            files[i] = RequestBody.create(MediaType.parse("image/png"), File(images[i]))
        }
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        builder
                .addFormDataPart("studentId", g.userId.toString() + "")
                .addFormDataPart("csId", g.categoryId.toString() + "")
                .addFormDataPart("gName", g.name!!)
                .addFormDataPart("gBuyPrice", g.originPrice!!)
                .addFormDataPart("gSellPrice", g.price!!)
                .addFormDataPart("gDesc", g.description!!)
                .addFormDataPart("coverImage", System.currentTimeMillis().toString() + "image.png", files[0])

        for (i in 1 until files.size) {
            builder.addFormDataPart("images", System.currentTimeMillis().toString() + "image.png", files[i])
                    .addFormDataPart("desc", "这个拿来干啥？？")
        }

        if (files.size == 1) {
            builder.addFormDataPart("desc", "只传了一张图片，没有描述")
        }

        val requestBody = builder.build()


        val responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/goods_upload", requestBody)
        val result = JSONUtil.parseJson<Result<String>>(responseData, object : TypeToken<Result<String>>() {

        })

        if (result != null) {
            if (result.code == 1) {
                callback.succeed(result.message!!)
            } else {
                callback.failed(result.message!!)
            }
        } else {
            callback.failed("error 服务器异常")
        }
    }

    override fun goodsSearch(callback: GeneralCallback<List<Goods>>, keyword: String) {
        api.goodsSearch(keyword).enqueue(MyRetrofitCallBack(callback))

    }

    override fun deleteGoods(callback: GeneralCallback<String>, goodsId: Int) {
        callback.succeed("删除成功")
    }

    override fun updateGoods(callback: GeneralCallback<String>, g: Goods) {
        val images = g.imagesPath
        val files = arrayOfNulls<RequestBody>(images!!.size)
        for (i in images.indices) {
            files[i] = RequestBody.create(MediaType.parse("image/png"), File(images[i]))
        }
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder
                .addFormDataPart("studentId", g.userId.toString() + "")
                .addFormDataPart("csId", g.categoryId.toString() + "")
                .addFormDataPart("gName", g.name!!)
                .addFormDataPart("gBuyPrice", g.originPrice!!)
                .addFormDataPart("gSellPrice", g.price!!)
                .addFormDataPart("gDesc", g.description!!)
                .addFormDataPart("coverImage", System.currentTimeMillis().toString() + "image.png", files[0])

        for (i in 1 until files.size) {
            builder.addFormDataPart("images", System.currentTimeMillis().toString() + "image.png", files[i])
                    .addFormDataPart("desc", "这个拿来干啥？？")
        }

        if (files.size == 1) {
            builder.addFormDataPart("desc", "只传了一张图片，没有描述")
        }

        val requestBody = builder.build()


        val responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/goods_upload", requestBody)
        val result = JSONUtil.parseJson<Result<String>>(responseData, object : TypeToken<Result<String>>() {

        })

        if (result != null) {
            if (result.code == 1) {
                callback.succeed(result.message!!)
            } else {
                callback.failed(result.message!!)
            }
        } else {
            callback.failed("error 服务器异常")
        }
    }

    override fun addReview(callback: GeneralCallback<Review>, review: Review) {
        val url = Config.ServerURL + "/22y/review_reviewSave"
        val requestbody = FormBody.Builder()
                .add("userId", review.userId!!)
                .add("goodsId", review.goodsId!!)
                .add("reviewContext", review.content!!)
                .build()
        val responseData = HttpService.getInstance().sendPostRequest(url, requestbody)
        val result = JSONUtil.parseJson<Result<*>>(responseData, object : TypeToken<Result<*>>() {

        })

        if (result != null) {
            if (result.code == 1) {
                callback.succeed(review)
            } else {
                callback.failed(result.message!!)
            }
        } else {
            callback.failed("error: 服务器异常")
        }
    }
}
