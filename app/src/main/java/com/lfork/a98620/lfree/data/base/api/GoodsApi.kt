package com.lfork.a98620.lfree.data.base.api

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.lfork.a98620.lfree.base.Config
import com.lfork.a98620.lfree.base.network.HttpService
import com.lfork.a98620.lfree.base.network.Result
import com.lfork.a98620.lfree.data.DataSource
import com.lfork.a98620.lfree.data.base.entity.*
import com.lfork.a98620.lfree.data.goods.remote.GoodsRemoteDataSource
import com.lfork.a98620.lfree.util.JSONUtil
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File
import java.util.ArrayList

/**
 *
 * Created by 98620 on 2018/9/11.
 */
interface GoodsApi {

    /**
     * 根据商品的种类和当前页面的用于定位的时间信息 @param cursor 来查询商品信息
     *
     * @param categoryId 商品种类id
     * @param cursor 当前显示商品的游标位置(商品的时间信息)
     * @return 返回的查询结果
     */
    @GET("22y/goodsApp_getGoodsPageApp")
    fun getGoodsList(@Query("csId") categoryId: Int, @Query("cursor") cursor: String): Call<Result<List<Goods>>>


    @GET("22y/user_getUserGoodsByUid")
    fun getUserGoodsList(@Query("studentId") userId: String, @Query("cursor") cursor: String): Call<Result<List<Goods>>>


    @GET("22y/cs_getCsList")
    fun getUserGoodsList(): Call<Result<List<Category>>>


    @GET("22y/goods_getGoodsById")
    fun getGoods(@Query("goodsId") goodsId: String): Call<Result<GoodsDetailInfo>>


    @GET("22y/goodsSerach_getGoodsByName")
    fun goodsSearch(@Query("goodsLikeName") keyword: String): Call<Result<List<Goods>>>


    /**
     * 添加商品评论
     *
     * @param username studentId,email,username都行
     * @param password 密码
     * @return 指定请求数据的Call对象
     */
    @FormUrlEncoded
    @POST("22y/review_reviewSave")
    fun addReview(
            @Field("userId") userId: String,
            @Field("goodsId") goodsId: String,
            @Field("reviewContext") content: String): Call<Result<Review>>


    /**
     * 更新用户头像 单文件上传实例
     * Part的普通键值对需要用RequestBody来写
     * RequestBody.create(null, studentId)
     * @param fileBody  .
     * @param studentId .
     * @return .
     */
    @POST("22y/goods_upload")
    fun uploadGoods(@Body multipartBody: MultipartBody): Call<Result<String>>


    companion object {
        fun create(): GoodsApi {
            val retrofit = HttpService.getRetrofitInstance()
            return retrofit.create(GoodsApi::class.java)
        }
    }
}