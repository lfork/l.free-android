package com.lfork.a98620.lfree.data.source.remote;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.UserDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 98620 on 2018/3/23.
 */

public class UserRemoteDataSource implements UserDataSource {

    private static UserRemoteDataSource INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "UserRemoteDataSource";

    private UserRemoteDataSource() {
        // dbHelper = null; //这里的Context 默认是用来进行数据库操作的
        // fileHelper = new MessageFileHelper();
//		UserRemoteDataSource.user = user;

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
    public void login(GeneralCallback<User> callback, User user) {

    }

    @Override
    public void register(GeneralCallback<String> callback, User user) {

    }

    @Override
    public void getThisUser() {

    }

    @Override
    public void updateThisUser() {

    }
}
