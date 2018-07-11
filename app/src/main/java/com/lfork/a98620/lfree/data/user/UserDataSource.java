package com.lfork.a98620.lfree.data.user;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.School;
import com.lfork.a98620.lfree.data.entity.User;

import java.util.List;

/**
 * Created by 98620 on 2018/3/23.
 **/

public interface UserDataSource extends DataSource {

    void register(GeneralCallback<String> callback, User user);

    void updateUserInfo(GeneralCallback<String> callback, User user);

    void updateUserPortrait(GeneralCallback<String> callback, String studentId, String localFilePath);

    void login(GeneralCallback<User> callback, User user);

    /**
     * 获取用户信息 userId为-1的时候表示获取当前用户信息
     * 当前用户信息需要存放在本地数据库(只保存部份关键信息)
     * @param callback .
     * @param userId userId
     */
    void getUserInfo(GeneralCallback<User> callback, int userId);

    void getSchoolList(GeneralCallback<List<School>> callback);

}
