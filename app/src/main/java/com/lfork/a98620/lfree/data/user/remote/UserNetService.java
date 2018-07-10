package com.lfork.a98620.lfree.data.user.remote;

import com.lfork.a98620.lfree.base.network.Result;
import com.lfork.a98620.lfree.data.entity.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 98620 on 2018/7/10.
 */
public interface UserNetService {

    @FormUrlEncoded
    @POST("22y/user_login")
    Call<Result<User>> login(@Field("studentId") String username, @Field("studentId") String password);

    /**
     *  @Override
    public void login(final GeneralCallback<User> callback, User user) {

    String url = Config.ServerURL + "/22y/user_login";

    RequestBody requestbody = new FormBody.Builder()
    .add("studentId", user.getUserName())
    .add("userPassword", user.getUserPassword())
    .build();

    String responseData = HttpService.getInstance().sendPostRequest(url, requestbody);

    Result<User> result = JSONUtil.parseJson(responseData, new TypeToken<Result<User>>() {
    });

    if (result != null) {
    User u = result.getData();
    if (u != null)
    callback.succeed(u);
    else {
    callback.failed(result.getMessage());
    }
    } else {
    callback.failed("error");
    }
    }
     */

}
