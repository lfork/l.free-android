package com.lfork.a98620.lfree.data.base.api

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.lfork.a98620.lfree.base.Config
import com.lfork.a98620.lfree.base.network.HttpService
import com.lfork.a98620.lfree.base.network.Result
import com.lfork.a98620.lfree.data.DataSource
import com.lfork.a98620.lfree.data.base.entity.Category
import com.lfork.a98620.lfree.data.base.entity.Goods
import com.lfork.a98620.lfree.data.base.entity.GoodsDetailInfo
import com.lfork.a98620.lfree.data.base.entity.User
import com.lfork.a98620.lfree.data.goods.remote.GoodsRemoteDataSource
import com.lfork.a98620.lfree.util.JSONUtil
import okhttp3.FormBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
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
    fun getGoods(@Query("goodsId") goodsId:String): Call<Result<GoodsDetailInfo>>


    @GET("22y/goodsSerach_getGoodsByName")
    fun goodsSearch(@Query("goodsLikeName") keyword:String): Call<Result<List<Goods>>>


    companion object {
        fun create() : GoodsApi {
            val retrofit = HttpService.getRetrofitInstance()
            return retrofit.create(GoodsApi::class.java)
        }
    }
}