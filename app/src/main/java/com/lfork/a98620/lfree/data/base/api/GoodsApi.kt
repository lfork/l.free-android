package com.lfork.a98620.lfree.data.base.api

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.lfork.a98620.lfree.base.Config
import com.lfork.a98620.lfree.base.network.HttpService
import com.lfork.a98620.lfree.base.network.Result
import com.lfork.a98620.lfree.data.DataSource
import com.lfork.a98620.lfree.data.base.entity.Goods
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
     * 根据Id获取用户的完整信息
     *
     * @param userId .
     * @return .
     */
    @GET("22y/user_info")
    fun getUserInfo(@Query("studentId") userId: String): Call<Result<User>>


    /**
     * 根据商品的种类和当前页面的用于定位的时间信息 @param cursor 来查询商品信息
     *
     * @param categoryId 商品种类id
     * @param cursor 当前显示商品的游标位置(商品的时间信息)
     * @return 返回的查询结果
     */
    @GET("22y/goodsApp_getGoodsPageApp")
    fun getGoodsList(@Query("csId") categoryId: Int, @Query("cursor") cursor: String): Call<Result<ArrayList<Goods>>>


//
//    override fun getGoodsList(callback: DataSource.GeneralCallback<List<Goods>>, cursor: String, categoryId: Int) {
//        val url = Config.ServerURL + "/22y/goodsApp_getGoodsPageApp"
//
//        Log.d(GoodsRemoteDataSource.TAG, "getGoodsList: $cursor")
//        val requestbody = FormBody.Builder()
//                .add("csId", categoryId.toString() + "")
//                .add("cursor", cursor + "")
//                .build()
//
//        val responseData = HttpService.getInstance().sendPostRequest(url, requestbody)
//
//        val result = JSONUtil.parseJson<Result<ArrayList<Goods>>>(responseData, object : TypeToken<Result<ArrayList<Goods>>>() {
//
//        })
//
//        if (result != null) {
//            val list = result.data
//            if (list != null && list.size > 0) {
//                callback.succeed(list)
//            } else {
//                callback.failed(result.message)
//            }
//        } else {
//            callback.failed("error：服务器异常、或者是没有网络连接")
//        }
//    }



    companion object {
        fun create() : GoodsApi {
            val retrofit = HttpService.getRetrofitInstance()
            return retrofit.create(GoodsApi::class.java)
        }
    }
}