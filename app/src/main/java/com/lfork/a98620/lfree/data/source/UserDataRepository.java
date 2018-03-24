package com.lfork.a98620.lfree.data.source;

import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.remote.UserRemoteDataSource;

/**
 *
 * Created by 98620 on 2018/3/23.
 */

public class UserDataRepository implements UserDataSource {

    private static UserDataRepository INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "UserDataRepository ";

    private UserRemoteDataSource remoteDataSource;

    private User mUser;

    private UserDataRepository(UserRemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
        mUser = new User();

    }

    public static UserDataRepository getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new UserDataRepository(UserRemoteDataSource.getInstance());
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
            public void success(User data) {
                callback.success(data);
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
    public void updateThisUser() {

    }
}
