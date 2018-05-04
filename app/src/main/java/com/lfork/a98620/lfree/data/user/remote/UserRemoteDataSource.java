package com.lfork.a98620.lfree.data.user.remote;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.base.network.httpservice.HttpService;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataSource;
import com.lfork.a98620.lfree.base.network.httpservice.Result;
import com.lfork.a98620.lfree.util.Config;
import com.lfork.a98620.lfree.util.JSONUtil;

import java.io.File;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by 98620 on 2018/3/23.
 */

public class UserRemoteDataSource implements UserDataSource {

    private static UserRemoteDataSource INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "IMRemoteDataSource";

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

        String url = Config.ServerURL +  "/22y/user_login";

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

    @Override
    public void register(GeneralCallback<String> callback, User user) {
        String url = Config.ServerURL + "/22y/user_regist";

        RequestBody requestbody = new FormBody.Builder()
                .add("studentId", user.getStudentId())
                .add("userPassword", user.getUserPassword())
                .add("userName", user.getUserName())
                .build();

        Log.d(TAG, "validate s: " + user.getStudentId() + " u:" + user.getUserName() + " p:" + user.getUserPassword());

        String responseData = HttpService.getInstance().sendPostRequest(url, requestbody);

        Result result = JSONUtil.parseJson(responseData, new TypeToken<Result>() {
        });

        if (result != null) {
            Log.d(TAG, "register: ");
            if (result.getCode() == 1) {
                callback.succeed(result.getMessage());
            } else {
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
    public void getThisUser(GeneralCallback<User> callback) {

    }

    @Override
    public boolean saveThisUser(User user) {
        return false;
    }


    @Override
    public void updateThisUser(GeneralCallback<String> callback, User user) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("studentId", user.getUserId() + "")
                .addFormDataPart("userEmail", user.getUserEmail())
                .addFormDataPart("userPhone", user.getUserPhone())
                .addFormDataPart("userName", user.getUserName())
                .build();
        String responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/user_save", requestBody);

        Result<User> result = JSONUtil.parseJson(responseData, new TypeToken<Result<User>>() {
        });

        if (result != null) {
            if (result.getCode() == 1) {
                callback.succeed(result.getMessage());
            } else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error 服务器异常");
        }
    }

    @Override
    public void updateUserPortrait(GeneralCallback<String> callback, String studentId, String localFilePath) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), new File(localFilePath));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.png", fileBody)
                .addFormDataPart("studentId", studentId)
                .build();
        String responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/user_imageUpload", requestBody);
        Result<String> result = JSONUtil.parseJson(responseData, new TypeToken<Result<String>>() {
        });

        if (result != null) {
            if (result.getCode() == 1) {
                callback.succeed(result.getData());
            } else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error 服务器异常");
        }
    }


    @Override
    public void getUserInfo(GeneralCallback<User> callback, int userId) {
        RequestBody requestBody = new FormBody.Builder()
                .add("studentId", userId+"")
                .build();
        String responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/user_info", requestBody);
        Result<User> result = JSONUtil.parseJson(responseData, new TypeToken<Result<User>>() {
        });

        if (result != null) {
            if (result.getCode() == 1) {
                callback.succeed(result.getData());
            } else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error 服务器异常");
        }

    }
}
