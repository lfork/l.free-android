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
    public void login(GeneralCallback<User> callback, User user) {
        remoteDataSource.login(callback, user);
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
