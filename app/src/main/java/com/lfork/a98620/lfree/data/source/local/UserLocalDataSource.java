package com.lfork.a98620.lfree.data.source.local;

import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.UserDataSource;

import org.litepal.crud.DataSupport;

/**
 * Created by 98620 on 2018/3/23.
 */

public class UserLocalDataSource implements UserDataSource {

    private static UserLocalDataSource INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "IMRemoteDataSource";

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
//        User user = (User)DataSupport.where("isLogin =  ?", "1").find(User.class);
        User user  = DataSupport.findFirst(User.class);

        if (user != null) {
            callback.succeed(user);
        } else {
            callback.failed("没有用户");
        }
    }

    @Override
    public boolean saveThisUser(User user) {
        try {
            DataSupport.deleteAll(User.class);
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
