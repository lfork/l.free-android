package com.lfork.a98620.lfree.data.base.api

import com.lfork.a98620.lfree.base.network.HttpService
import com.lfork.a98620.lfree.base.network.Result
import com.lfork.a98620.lfree.data.base.entity.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 *
 * Created by 98620 on 2018/8/26.
 */
interface UserApi {

    /**
     * 用户登录
     *
     * @param username studentId,email,username都行
     * @param password 密码
     * @return 指定请求数据的Call对象
     */
    @FormUrlEncoded
    @POST("22y/user_login")
    fun login(
            @Field("studentId") username: String,
            @Field("userPassword") password: String): Call<Result<User>>


    /**
     * 用户注册
     *
     * @param studentId .
     * @param password  .
     * @param username  .
     * @param schoolId  .
     * @return 指定请求数据的Call对象
     */
    @FormUrlEncoded
    @POST("22y/user_regist")
    fun register(
            @Field("studentId") studentId: String,
            @Field("userPassword") password: String,
            @Field("userName") username: String,
            @Field("userSchool.id") schoolId: String): Call<Result<String>>

    /**
     * 根据Id获取用户的完整信息
     *
     * @param userId .
     * @return .
     */
    @GET("22y/user_info")
    fun getUserInfo(@Query("studentId") userId: String): Call<Result<User>>


    /**
     * 更新用户头像 单文件上传实例
     * Part的普通键值对需要用RequestBody来写
     * RequestBody.create(null, studentId)
     * @param fileBody  .
     * @param studentId .
     * @return .
     */
    @Multipart
    @POST("22y/user_imageUpload")
    fun updatePortrait(
            @Part fileBody: MultipartBody.Part,
            @Part("studentId") studentId: RequestBody): Call<Result<String>>

    /**
     * @param studentId .
     * @param username .
     * @param schoolId .
     * @param dec .
     * @param email .
     * @param phone .
     * @return .
     */
    @FormUrlEncoded
    @POST("22y/user_save")
    fun updateUserInfo(
            @Field("studentId") studentId: Int,
            @Field("userName") username: String,
            @Field("userSchool.id") schoolId: String,
            @Field("userDesc") dec: String,
            @Field("userEmail") email: String,
            @Field("userPhone") phone: String
    ): Call<Result<User>>

    companion object {
        fun create() : UserApi {
            val retrofit = HttpService.getRetrofitInstance()
            return retrofit.create(UserApi::class.java)
        }
    }

}