package com.lfork.a98620.lfree.data.imdata.local

import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.base.entity.User
import com.lfork.a98620.lfree.data.imdata.IMDataSource
import com.lfork.a98620.lfree.imservice.request.LoginListener
import org.litepal.crud.DataSupport

/**
 *
 * Created by 98620 on 2018/9/3.
 */

class IMLocalDataSource : IMDataSource {

    /**
     * 伪单例、不需要线程同步、懒汉模式
     */
    companion object {
        private var INSTANCE: IMLocalDataSource? = null
        fun getInstance(): IMLocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = IMLocalDataSource()
            }
            return INSTANCE as IMLocalDataSource
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    override fun login(user: User, listener: LoginListener) {
    }

    override fun logout(userId: Int, callback: GeneralCallback<String>) {
    }

    override fun getChatUserList(callback: GeneralCallback<List<User>>) {
        val userList = DataSupport.where("ischatuser=1").order("timestamp desc").find(User::class.java)
        if (userList != null) {
            callback.succeed(userList)
        } else {
            callback.failed("没有数据")
        }
    }

    override fun addChatUser(user: User, callback: GeneralCallback<String>) {
        user.isChatUser = true
        user.timestamp = System.currentTimeMillis()
        user.save()
        callback.succeed("添加成功")
    }

    override fun addChatUser(user: User, isExisted: Boolean, callback: GeneralCallback<String>) {
    }

    override fun removeChatUser(userId: Int, callback: GeneralCallback<List<User>>) {
        DataSupport.deleteAll(User::class.java, "userid = ?", userId.toString() + "")
        callback.succeed(emptyList())
    }

}
