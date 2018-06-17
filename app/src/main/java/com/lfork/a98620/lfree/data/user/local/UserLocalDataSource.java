package com.lfork.a98620.lfree.data.user.local;

import android.util.Log;

import com.lfork.a98620.lfree.data.entity.School;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataSource;

import org.litepal.crud.DataSupport;

import java.util.List;

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
        List<User> userList = DataSupport.where("isLogin=?", "1").find(User.class);
        User user = null;
        if (userList != null && userList.size() > 0)
            user = userList.get(0);

        if (user != null) {
            callback.succeed(user);
        } else {
            callback.failed("没有用户");
        }
    }

    @Override
    public boolean saveThisUser(User user) {
        try {
            DataSupport.deleteAll(User.class, "islogin=0 and userid=? ",user.getUserId()+"");
            //lie
//            user.setId(0);
            boolean result = user.save();
            Log.d(TAG, "saveThisUser: 用户信息本地更新成功" + result);
            return result;
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
       List<User> list = DataSupport.where("userid=?", userId + "").find(User.class);
       if (list != null &&list.size() > 0) {
           callback.succeed(list.get(0));
       } else {
           callback.failed("没有数据");
       }
    }

    @Override
    public void getSchoolList(GeneralCallback<List<School>> callback) {

    }
}
