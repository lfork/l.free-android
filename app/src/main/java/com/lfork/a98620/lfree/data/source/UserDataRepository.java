package com.lfork.a98620.lfree.data.source;

import com.lfork.a98620.lfree.data.entity.User;

/**
 *
 * Created by 98620 on 2018/3/23.
 */

public class UserDataRepository implements UserDataSource {

    private static UserDataRepository INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "UserDataRepository ";

    private User mUser;

    private UserDataRepository() {
        // dbHelper = null; //这里的Context 默认是用来进行数据库操作的
        // fileHelper = new MessageFileHelper();
//		UserDataRepository .user = user;
        mUser = new User();

    }

    public static UserDataRepository getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new UserDataRepository();
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
