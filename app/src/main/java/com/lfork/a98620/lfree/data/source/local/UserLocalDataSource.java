package com.lfork.a98620.lfree.data.source.local;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.UserDataSource;
import com.lfork.a98620.lfree.data.source.remote.httpservice.Result;
import com.lfork.a98620.lfree.data.source.remote.httpservice.Service;
import com.lfork.a98620.lfree.util.JSONUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by 98620 on 2018/3/23.
 */

public class UserLocalDataSource implements UserDataSource {

    private static UserLocalDataSource INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "UserRemoteDataSource";

    private UserLocalDataSource() {
    }

    public static UserLocalDataSource getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new UserLocalDataSource();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void login(final GeneralCallback<User> callback, User user) {
    }

    @Override
    public void register(GeneralCallback<String> callback, User user) {


    }

    @Override
    public User getThisUser() {
        return null;
    }

    @Override
    public void getThisUser(GeneralCallback<User> callback) {
        List<User> userList = DataSupport.findAll(User.class);
        if (userList.size() > 0) {
            callback.success(userList.get(0));
        } else {
            callback.failed("没有用户");
        }
    }

    @Override
    public boolean saveThisUser(User user) {
        try {
//            DataSupport.deleteAll(User.class);
            return user.save();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void updateThisUser(GeneralCallback<String> callback, User user) {
        user.update(user.getId());

    }

    @Override
    public void updateUserPortrait(GeneralCallback<String> callback, String studentId, String localFilePath) {

    }

    @Override
    public void getUserInfo(GeneralCallback<User> callback, int userId) {

    }
}
