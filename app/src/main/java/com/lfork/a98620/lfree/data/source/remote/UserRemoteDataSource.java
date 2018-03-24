package com.lfork.a98620.lfree.data.source.remote;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.UserDataSource;
import com.lfork.a98620.lfree.data.source.remote.httpservice.Result;
import com.lfork.a98620.lfree.data.source.remote.httpservice.Service;
import com.lfork.a98620.lfree.util.JSONUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by 98620 on 2018/3/23.
 */

public class UserRemoteDataSource implements UserDataSource {

    private static UserRemoteDataSource INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "UserRemoteDataSource";

    private UserRemoteDataSource() {
    }

    public static UserRemoteDataSource getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new UserRemoteDataSource();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void login(final GeneralCallback<User> callback, User user) {

        String url = "http://www.lfork.top/22y/user_login";

        RequestBody requestbody = new FormBody.Builder()
                .add("studentId", user.getUserName())
                .add("userPassword", user.getUserPassword())
                .build();

        String responseData = new Service().sendPostRequest(url, requestbody);

        Result<User> result = JSONUtil.parseJson(responseData, new TypeToken<Result<User>>() {
        });

        if (result != null) {
            User u = result.getData();
            if (u != null)
                callback.success(u);
            else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error");
        }
    }

    @Override
    public void register(GeneralCallback<String> callback, User user) {
        String url = "http://www.lfork.top/22y/user_regist";

        RequestBody requestbody = new FormBody.Builder()
                .add("studentId", user.getStudentId())
                .add("userPassword", user.getUserPassword())
                .add("userName", user.getUserName())
                .build();

        Log.d(TAG, "validate s: " + user.getStudentId() + " u:" + user.getUserName() + " p:" + user.getUserPassword());

        String responseData = new Service().sendPostRequest(url, requestbody);

        Result result = JSONUtil.parseJson(responseData, new TypeToken<Result>() {
        });

        if (result != null) {
            if (result.getCode() == 1)
                callback.success(result.getMessage());
            else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error 服务器异常");
        }


    }

    @Override
    public User getThisUser() {
        return null;
    }

    @Override
    public void updateThisUser() {

    }
}
