package com.lfork.a98620.lfree.data.user

import com.lfork.a98620.lfree.data.DataSource
import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.base.entity.School
import com.lfork.a98620.lfree.data.base.entity.User

/**
 *
 * Created by 98620 on 2018/8/30.
 */
interface UserDataSource : DataSource{
    /**
     * apply an account
     * @param callback call back with string result
     * @param user user information
     */
    fun register(callback: GeneralCallback<String>, user: User)

    /**
     * update user information
     * @param callback call back with string result
     * @param user user information
     */
    fun updateUserInfo(callback: GeneralCallback<User>, user: User)

    /**
     * update user photo
     * @param callback  call back with string result
     * @param studentId user id
     * @param localFilePath local file path
     */
    fun updateUserPortrait(callback: GeneralCallback<String>, studentId: String, localFilePath: String)

    /**
     * do login
     * @param callback call back with User object
     * @param user user information
     */
    fun login(callback: GeneralCallback<User>, user: User)

    /**
     * 获取用户信息 userId为-1的时候表示获取当前用户信息
     * 当前用户信息需要存放在本地数据库(只保存部份关键信息)
     * @param callback .
     * @param userId userId
     */
    fun getUserInfo(callback: GeneralCallback<User>, userId: Int)

    /**
     * get schools information
     * @param callback call back with userSchool information list.
     */
    fun getSchoolList(callback: GeneralCallback<List<School>>)
}