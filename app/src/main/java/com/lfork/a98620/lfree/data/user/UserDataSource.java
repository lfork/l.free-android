package com.lfork.a98620.lfree.data.user;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;

/**
 * Created by 98620 on 2018/3/23.
 **/

public interface UserDataSource extends DataSource {
    void login(GeneralCallback<User> callback, User user);

    void register(GeneralCallback<String> callback, User user);

    User getThisUser();

    /**
     * 从本地数据库获取当前用户信息
     * @param callback
     */
    void getThisUser(GeneralCallback<User> callback);

    /**
     * 将已经登录的用户保存到数据库
     */
    boolean saveThisUser(User user);

    void updateThisUser(GeneralCallback<String> callback, User user);

    void updateUserPortrait(GeneralCallback<String> callback, String studentId, String localFilePath);

    void getUserInfo(GeneralCallback<User> callback, int userId);

}
