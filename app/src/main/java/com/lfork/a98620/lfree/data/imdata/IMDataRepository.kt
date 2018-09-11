package com.lfork.a98620.lfree.data.imdata

import com.lfork.a98620.lfree.base.FreeApplication
import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.base.entity.User
import com.lfork.a98620.lfree.data.imdata.local.IMLocalDataSource
import com.lfork.a98620.lfree.data.imdata.remote.IMRemoteDataSource
import com.lfork.a98620.lfree.imservice.MessageService
import com.lfork.a98620.lfree.imservice.request.LoginListener
import java.util.*

/**
 * Created by 98620 on 2018/9/3.
 */

class IMDataRepository : IMDataSource {
    private val mRemoteDataSource = IMRemoteDataSource.getInstance()
    private val mLocalDataSource = IMLocalDataSource.getInstance()

    /**
     * 伪单例、不需要线程同步、懒汉模式
     */
    companion object {
        private var INSTANCE: IMDataRepository? = null
        fun getInstance(): IMDataRepository {
            if (INSTANCE == null) {
                INSTANCE = IMDataRepository()
            }
            return INSTANCE as IMDataRepository
        }

        fun destroyInstance() {
            IMRemoteDataSource.destroyInstance()
            IMLocalDataSource.destroyInstance()
            INSTANCE = null
        }
    }
    private var mCachedUser: User? = null

    /**
     * This variable has package local visibility so it can be accessed from tests.  重点来了，写成map的话不会报错
     */
    internal var mCachedUsers: MutableMap<String, User>? = null


    private var mCachedUsersIsDirty: Boolean = false

    var binder: MessageService.MessageBinder? = null
        private set

    fun setServiceBinder(binder: MessageService.MessageBinder) {
        this.binder = binder
    }

    override fun login(user: User, listener: LoginListener) {
        mRemoteDataSource.login(user, object : LoginListener {
            override fun succeed(user: User) {
                //如果是Android的话 这里记得要返回UI线程  使用Handler或者是Handler的封装
                mCachedUser = user
                listener.succeed(user)
            }

            override fun failed(log: String) {
                //如果是Android的话 这里记得要返回UI线程  使用Handler或者是Handler的封装
                listener.failed(log)
            }
        })

    }

    override fun logout(userId: Int, result: GeneralCallback<String>) {
        FreeApplication.executeThreadInDefaultThreadPool { mRemoteDataSource.logout(userId, result) }
    }

    @Synchronized
    override fun getChatUserList(callback: GeneralCallback<List<User>>) {
        if (mCachedUsers != null) {
            val users = ArrayList(mCachedUsers!!.values)
            users.sortWith(Comparator {           //小的排前面
                o1, o2 ->
                if (o1.timestamp > o2.timestamp) {
                    - 1
                } else {
                    1
                }
            })

            callback.succeed(users)
            return
        }
        mLocalDataSource.getChatUserList(object : GeneralCallback<List<User>> {
            override fun succeed(data: List<User>) {
                refreshCache(data)
                callback.succeed(data)
            }

            override fun failed(log: String) {
                callback.failed(log)
            }
        })
    }

    @Synchronized
    override fun addChatUser(user: User, callback: GeneralCallback<String>) {

    }

    @Synchronized
    override fun addChatUser(user: User, isExisted: Boolean, callback: GeneralCallback<String>) {
        if (isExisted) {
            if (mCachedUsers != null) {
                mCachedUsers!!.remove(user.userId.toString() + "")
                addChatUserInLocal(user, callback)
            }
        } else {
            addChatUserInLocal(user, callback)
        }
    }

    @Synchronized
    private fun addChatUserInLocal(user: User, callback: GeneralCallback<String>) {
        mLocalDataSource.addChatUser(user, object : GeneralCallback<String> {
            override fun succeed(data: String) {

                if (mCachedUsers == null) {
                    mCachedUsers = LinkedHashMap()
                }
                mCachedUsers!![user.userId.toString() + ""] = user
                callback.succeed(data)
            }

            override fun failed(log: String) {
                callback.failed(log)

            }
        })

    }

    @Synchronized
    override fun removeChatUser(userId: Int, callback: GeneralCallback<List<User>>) {
        mLocalDataSource.removeChatUser(userId, object : GeneralCallback<List<User>> {
            override fun succeed(data: List<User>) {

                if (mCachedUsers != null) {
                    mCachedUsers!!.remove(userId.toString() + "")
                    callback.succeed(ArrayList(mCachedUsers!!.values))
                } else {
                    callback.failed("没有数据")
                }


            }

            override fun failed(log: String) {
                callback.failed(log)
            }
        })

    }

    private fun refreshCache(users: List<User>) {
        if (mCachedUsers == null) {
            mCachedUsers = LinkedHashMap()
        }
        mCachedUsers!!.clear()
        for (user in users) {
            mCachedUsers!![user.userId.toString() + ""] = user
        }
        mCachedUsersIsDirty = false
    }


    @Synchronized
    fun isUserExisted(userId: Int): Boolean {
        if (mCachedUsers != null) {
            val user = mCachedUsers!![userId.toString() + ""]
            return user != null
        }
        return false
    }


}
