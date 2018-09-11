package com.lfork.a98620.lfree.data.imdata

import com.lfork.a98620.lfree.data.DataSource
import com.lfork.a98620.lfree.data.base.entity.User
import com.lfork.a98620.lfree.imservice.request.LoginListener

/**
 *
 * Created by 98620 on 2018/9/3.
 */

interface IMDataSource : DataSource {

    /**
     * 登录成功后，客户端会马上请求用户的详细信息(当然，不包含密码)。 //TODO请求完毕后需要更新TCP本地连接的用户信息
     */
    fun login(user: User, listener: LoginListener)

    fun logout(userId: Int, result: DataSource.GeneralCallback<String>)

    fun getChatUserList(callback: DataSource.GeneralCallback<List<User>>)

    fun addChatUser(user: User, callback: DataSource.GeneralCallback<String>)

    fun addChatUser(user: User, isExisted: Boolean, callback: DataSource.GeneralCallback<String>)

    fun removeChatUser(userId: Int, callback: DataSource.GeneralCallback<List<User>>)

}
