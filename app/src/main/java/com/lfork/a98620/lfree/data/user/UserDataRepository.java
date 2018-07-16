package com.lfork.a98620.lfree.data.user;

import com.lfork.a98620.lfree.data.entity.School;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.local.UserLocalDataSource;
import com.lfork.a98620.lfree.data.user.remote.UserRemoteDataSource;

import java.util.List;

/**
 *
 * @author 98620
 * @date 2018/3/23
 */

public class UserDataRepository implements UserDataSource {

    private static UserDataRepository INSTANCE;

    private UserRemoteDataSource remoteDataSource;

    private UserLocalDataSource localDataSource;

    private User mCachedUser;

    private boolean mCachedUserIsDirty = true;

    private int userId;

    private UserDataRepository(UserRemoteDataSource remoteDataSource, UserLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        //当数据仓库初始化的时候，同时也需要异步初始化用户数据。
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

    @Override
    public void login(final GeneralCallback<User> callback, User user) {
        remoteDataSource.login(new GeneralCallback<User>() {
            @Override
            public void succeed(User data) {
                //只需要登录成功的UserId即可
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
    public void updateUserInfo(GeneralCallback<String> callback, User user) {
        remoteDataSource.updateUserInfo(new GeneralCallback<String>() {
            @Override
            public void succeed(String data) {
                mCachedUserIsDirty = true;
                callback.succeed(data);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);
            }
        }, user);
    }

    @Override
    public void updateUserPortrait(GeneralCallback<String> callback, String studentId, String localFilePath) {
        remoteDataSource.updateUserPortrait(new GeneralCallback<String>() {
            @Override
            public void succeed(String data) {
                mCachedUserIsDirty = true;
                callback.succeed(data);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);
            }
        }, studentId, localFilePath);
    }

    /**
     * 还是可以做下当前用户信息的内存缓存
     *
     * @param callback .
     * @param userId   userId
     */
    @Override
    public void getUserInfo(GeneralCallback<User> callback, int userId) {
        if (userId != getUserId()) {
            remoteDataSource.getUserInfo(callback, userId);
            return;
        }

        // 对当前用户做的缓存
        if (mCachedUserIsDirty) {
            remoteDataSource.getUserInfo(new GeneralCallback<User>() {
                @Override
                public void succeed(User data) {
                    mCachedUser = data;
                    mCachedUserIsDirty = false;
                    callback.succeed(mCachedUser);
                }
                @Override
                public void failed(String log) {
                    callback.failed(log);
                }
            }, userId);
        } else {
            callback.succeed(mCachedUser);
        }
    }

    public void getUserInfo(int userId, boolean isCached, GeneralCallback<User> callback) {
        if (isCached) {
            localDataSource.getUserInfo(callback, userId);
        } else {
            remoteDataSource.getUserInfo(callback, userId);
        }

    }

    @Override
    public void getSchoolList(GeneralCallback<List<School>> callback) {
        remoteDataSource.getSchoolList(callback);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
