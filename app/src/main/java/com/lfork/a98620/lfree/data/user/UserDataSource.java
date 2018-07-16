package com.lfork.a98620.lfree.data.user;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.School;
import com.lfork.a98620.lfree.data.entity.User;

import java.util.List;

/**
 *
 * @author 98620
 * @date 2018/3/23
 **/

public interface UserDataSource extends DataSource {

    /**
     * apply an account
     * @param callback call back with string result
     * @param user user information
     */
    void register(GeneralCallback<String> callback, User user);

    /**
     * update user information
     * @param callback call back with string result
     * @param user user information
     */
    void updateUserInfo(GeneralCallback<String> callback, User user);

    /**
     * update user photo
     * @param callback  call back with string result
     * @param studentId user id
     * @param localFilePath local file path
     */
    void updateUserPortrait(GeneralCallback<String> callback, String studentId, String localFilePath);

    /**
     * do login
     * @param callback call back with User object
     * @param user user information
     */
    void login(GeneralCallback<User> callback, User user);

    /**
     * 获取用户信息 userId为-1的时候表示获取当前用户信息
     * 当前用户信息需要存放在本地数据库(只保存部份关键信息)
     * @param callback .
     * @param userId userId
     */
    void getUserInfo(GeneralCallback<User> callback, int userId);

    /**
     * get schools information
     * @param callback call back with school information list.
     */
    void getSchoolList(GeneralCallback<List<School>> callback);
}
