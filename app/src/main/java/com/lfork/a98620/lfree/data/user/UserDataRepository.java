package com.lfork.a98620.lfree.data.user;

import android.util.Log;

import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.local.UserLocalDataSource;
import com.lfork.a98620.lfree.data.user.remote.UserRemoteDataSource;

/**
 * Created by 98620 on 2018/3/23.
 */

public class UserDataRepository implements UserDataSource {

    private static UserDataRepository INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "UserDataRepository ";

    private UserRemoteDataSource remoteDataSource;
    private UserLocalDataSource localDataSource;

    private User mUser;

    private int userId;

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
            public void succeed(User data) {
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
            public void succeed(User data) {
                Log.d(TAG, "onQuit: " + UserDataRepository.getInstance().hashCode());
                data.setLogin(true);
                saveThisUser(data);
                mUser = data;
                setUserId(mUser.getUserId());
                callback.succeed(data);
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
        if (mUser != null){
            callback.succeed(mUser);
            return;
        }
        localDataSource.getThisUser(new GeneralCallback<User>() {
            @Override
            public void succeed(User data) {
                mUser = data;
                callback.succeed(data);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);
            }
        });
    }

    @Override
    public boolean saveThisUser(User user) {
        return localDataSource.saveThisUser(user);
    }

    @Override
    public void updateThisUser(GeneralCallback<String> callback, User user) {
        remoteDataSource.updateThisUser(new GeneralCallback<String>() {
            @Override
            public void succeed(String data) {
                callback.succeed(data);
                updateLocalUserInfo(user);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);

            }
        }, user);
    }

    @Override
    public void updateUserPortrait(GeneralCallback<String> callback, String studentId, String localFilePath) {
        remoteDataSource.updateUserPortrait(callback, studentId, localFilePath);
    }

    @Override
    public void getUserInfo(GeneralCallback<User> callback, int userId) {
        remoteDataSource.getUserInfo(callback, userId);
    }



    private void updateLocalUserInfo(User newUser) {

        //更新本地的user信息
        mUser.setUserPhone(newUser.getUserPhone());
        mUser.setUserEmail(newUser.getUserEmail());
        mUser.setUserDesc(newUser.getUserDesc());
        saveThisUser(mUser);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
