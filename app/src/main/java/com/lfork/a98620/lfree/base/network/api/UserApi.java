package com.lfork.a98620.lfree.base.network.api;

import com.lfork.a98620.lfree.base.network.Result;
import com.lfork.a98620.lfree.data.entity.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @author 98620
 * @date 2018/7/10
 */
public interface UserApi {
    /**
     * 用户登录
     *
     * @param username studentId,email,username都行
     * @param password 密码
     * @return 指定请求数据的Call对象
     */
    @FormUrlEncoded
    @POST("22y/user_login")
    Call<Result<User>> login(
            @Field("studentId") String username,
            @Field("userPassword") String password);


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
    Call<Result> register(
            @Field("studentId") String studentId,
            @Field("userPassword") String password,
            @Field("userName") String username,
            @Field("userSchool.id") String schoolId);

    /**
     * 根据Id获取用户的完整信息
     *
     * @param userId .
     * @return .
     */
    @GET("22y/user_info")
    Call<Result<User>> getUserInfo(@Query("studentId") String userId);


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
    Call<Result<String>> updatePortrait(
            @Part MultipartBody.Part fileBody,
            @Part("studentId") RequestBody studentId);

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
    Call<Result<User>> updateUserInfo(
            @Field("studentId") int studentId,
            @Field("userName") String username,
            @Field("userSchool.id") String schoolId,
            @Field("userDesc") String dec,
            @Field("userEmail") String email,
            @Field("userPhone") String phone
    );
}