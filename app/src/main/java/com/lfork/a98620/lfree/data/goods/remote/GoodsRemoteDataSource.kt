package com.lfork.a98620.lfree.data.goods.remote

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.lfork.a98620.lfree.base.Config
import com.lfork.a98620.lfree.base.network.HttpService
import com.lfork.a98620.lfree.base.network.Result
import com.lfork.a98620.lfree.base.network.api.GoodsApi
import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.entity.Category
import com.lfork.a98620.lfree.data.entity.Goods
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo
import com.lfork.a98620.lfree.data.entity.Review
import com.lfork.a98620.lfree.data.goods.GoodsDataSource
import com.lfork.a98620.lfree.util.JSONUtil
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

/**
 *
 * Created by 98620 on 2018/9/2.
 */

object GoodsRemoteDataSource : GoodsDataSource {
    override fun destroyInstance() {
       //Do nothing here temporarily
    }

    private val TAG = "IMRemoteDataSource"

    private val api: GoodsApi

    init {
        api = HttpService.getNetWorkService(GoodsApi::class.java)
    }

    override fun getGoodsList(callback: GeneralCallback<List<Goods>>, cursor: String, categoryId: Int) {
        val url = Config.ServerURL + "/22y/goodsApp_getGoodsPageApp"

        Log.d(TAG, "getGoodsList: $cursor")
        val requestbody = FormBody.Builder()
                .add("csId", categoryId.toString() + "")
                .add("cursor", cursor + "")
                .build()

        val responseData = HttpService.getInstance().sendPostRequest(url, requestbody)

        val result = JSONUtil.parseJson<Result<ArrayList<Goods>>>(responseData, object : TypeToken<Result<ArrayList<Goods>>>() {

        })

        if (result != null) {
            val list = result.data
            if (list != null && list.size > 0) {
                callback.succeed(list)
            } else {
                callback.failed(result.message)
            }
        } else {
            callback.failed("error：服务器异常、或者是没有网络连接")
        }
    }

    override fun getUserGoodsList(callback: GeneralCallback<List<Goods>>, cursor: String, userId: String) {
        val url = Config.ServerURL + "/22y/user_getUserGoodsByUid"
        // http://www.lfork.top/22y/user_getUserGoodsByUid?studentId=2015215064&cursor=2018-04-08%2008:03:07
        val requestbody = FormBody.Builder()
                .add("studentId", userId)
                .add("cursor", cursor)
                .build()

        val responseData = HttpService.getInstance().sendPostRequest(url, requestbody)

        val result = JSONUtil.parseJson<Result<ArrayList<Goods>>>(responseData, object : TypeToken<Result<ArrayList<Goods>>>() {

        })

        if (result != null) {
            val list = result.data
            if (list != null && list.size > 0) {
                callback.succeed(list)
            } else {
                callback.failed(result.message)
            }
        } else {
            callback.failed("error：服务器异常、或者是没有网络连接")
        }
    }

    override fun getCategories(callback: GeneralCallback<List<Category>>) {
        val url = Config.ServerURL + "/22y/cs_getCsList"

        val requestbody = FormBody.Builder()
                .build()
        val responseData = HttpService.getInstance().sendPostRequest(url, requestbody)
        val result = JSONUtil.parseJson<Result<List<Category>>>(responseData, object : TypeToken<Result<List<Category>>>() {

        })

        if (result != null) {
            val list = result.data
            if (list != null) {
                callback.succeed(list)
            } else {
                callback.failed(result.message)
            }
        } else {
            callback.failed("error:1 服务器异常")
        }
    }

    override fun getGoods(callback: GeneralCallback<GoodsDetailInfo>, goodsId: Int) {
        val url = Config.ServerURL + "/22y/goods_getGoodsById"

        val requestbody = FormBody.Builder()
                .add("goodsId", goodsId.toString() + "")
                .build()
        val responseData = HttpService.getInstance().sendPostRequest(url, requestbody)
        val result = JSONUtil.parseJson<Result<GoodsDetailInfo>>(responseData, object : TypeToken<Result<GoodsDetailInfo>>() {

        })

        if (result != null) {
            if (result.code == 1) {
                callback.succeed(result.data)
            } else {
                callback.failed(result.message)
            }
        } else {
            callback.failed("error: 服务器异常")
        }

    }

    override fun uploadGoods(callback: GeneralCallback<String>, g: Goods) {
        val images = g.imagesPath
        Log.d(TAG, "uploadGoods: " + g.publishDate!!)

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
                callback.succeed(result.message)
            } else {
                callback.failed(result.message)
            }
        } else {
            callback.failed("error 服务器异常")
        }
    }

    override fun goodsSearch(callback: GeneralCallback<List<Goods>>, keyword: String) {
        val url = Config.ServerURL + "/22y/goodsSerach_getGoodsByName"
        val requestbody = FormBody.Builder()
                .add("goodsLikeName", keyword)
                .build()
        val responseData = HttpService.getInstance().sendPostRequest(url, requestbody)
        val result = JSONUtil.parseJson<Result<List<Goods>>>(responseData, object : TypeToken<Result<List<Goods>>>() {

        })

        if (result != null) {
            if (result.code == 1) {
                callback.succeed(result.data)
            } else {
                callback.failed(result.message)
            }
        } else {
            callback.failed("error: 服务器异常")
        }

        //http://www.lfork.top/22y/goodsSerach_getGoodsByName?=Java
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
                callback.succeed(result.message)
            } else {
                callback.failed(result.message)
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
                callback.failed(result.message)
            }
        } else {
            callback.failed("error: 服务器异常")
        }
    }
}
