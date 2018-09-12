package com.lfork.a98620.lfree.base.network

import com.lfork.a98620.lfree.data.DataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * Created by 98620 on 2018/9/12.
 *
 *
 */
class MyRetrofitCallBack<T>(private val generalCallback: DataSource.GeneralCallback<T>) : Callback<Result<T>> {
    override fun onFailure(call: Call<Result<T>>, t: Throwable) {
        generalCallback.failed("error：服务器异常、或者是没有网络连接")
    }

    override fun onResponse(call: Call<Result<T>>, response: Response<Result<T>>) {
        Result.deal(response.body() as Result<T>, generalCallback)
    }
}