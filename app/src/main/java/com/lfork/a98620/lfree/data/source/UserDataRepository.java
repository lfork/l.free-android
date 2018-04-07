package com.lfork.a98620.lfree.data.source;

import android.util.Log;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.local.UserLocalDataSource;
import com.lfork.a98620.lfree.data.source.remote.UserRemoteDataSource;

import org.litepal.crud.DataSupport;

/**
 *
 * Created by 98620 on 2018/3/23.
 */

public class UserDataRepository implements UserDataSource {

    private static UserDataRepository INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "UserDataRepository ";

    private UserRemoteDataSource remoteDataSource;
    private UserLocalDataSource localDataSource;

    private User mUser;

    private UserDataRepository(UserRemoteDataSource remoteDataSource, UserLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;

        //当数据仓库初始化的时候，同时也需要异步初始化用户数据。
        initUser();

    }


    public static UserDataRepository getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new UserDataRepository(UserRemoteDataSource.getInstance(), UserLocalDataSource.getInstance());
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private void initUser() {
        new Thread(() -> getThisUser(new GeneralCallback<User>() {
            @Override
            public void success(User data) {
                mUser = data;
            }

            @Override
            public void failed(String log) {
                Log.d(TAG, "failed: " + log);
            }
        })
        ).start();

    }


    @Override
    public void login(final GeneralCallback<User> callback, User user) {
        remoteDataSource.login(new GeneralCallback<User>() {
            @Override
            public void success(User data) {
                callback.success(data);
                saveThisUser(data);
                mUser = data;
            }

            @Override
            public void failed(String log) {
                callback.failed(log);
            }
        }, user);
    }

    @Override
    public void register(GeneralCallback<String> callback, User user) {
        remoteDataSource.register(callback, user);
    }

    @Override
    public User getThisUser() {
        return mUser;
    }

    @Override
    public void getThisUser(GeneralCallback<User> callback) {
        localDataSource.getThisUser(callback);
    }

    @Override
    public boolean saveThisUser(User user) {
        return localDataSource.saveThisUser(user);
    }

    @Override
    public void updateThisUser(GeneralCallback<String> callback, User user) {
        localDataSource.updateThisUser(new GeneralCallback<String>() {
            @Override
            public void success(String data) {
                Log.d(TAG, "success: " + data);
            }

            @Override
            public void failed(String log) {
                Log.d(TAG, "failed: " + log);
            }
        }, user);
    }
}
