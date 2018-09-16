package com.lfork.a98620.lfree.data.user

import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.base.entity.School
import com.lfork.a98620.lfree.data.base.entity.User
import com.lfork.a98620.lfree.data.user.local.UserLocalDataSource
import com.lfork.a98620.lfree.data.user.remote.UserRemoteDataSource

/**
 *
 * Created by 98620 on 2018/8/30.
 */
object UserDataRepository : UserDataSource {
    //当数据仓库初始化的时候，同时也需要异步初始化用户数据。
    private val remoteDataSource = UserRemoteDataSource

    private val localDataSource = UserLocalDataSource

    var mCachedUser: User? = null

    private var mCachedUserIsDirty = true

    var userId: Int = 0

    /**
     * Clear cached data
     */
    fun destroyInstance(){
        remoteDataSource.destroyInstance()
        localDataSource.destroyInstance()
        mCachedUser = null;
        mCachedUserIsDirty = true
        userId = 0
    }

    override fun login(callback: GeneralCallback<User>, user: User) {
        remoteDataSource.login(object : GeneralCallback<User> {
            override fun succeed(data: User) {
                //只需要登录成功的UserId即可
                callback.succeed(data)

            }

            override fun failed(log: String) {
                callback.failed(log)
            }
        }, user)
    }

    override fun register(callback: GeneralCallback<String>, user: User) {
        remoteDataSource.register(callback, user)
    }

    override fun updateUserInfo(callback: GeneralCallback<User>, user: User) {
        remoteDataSource.updateUserInfo(object : GeneralCallback<User> {
            override fun succeed(data: User) {
                mCachedUserIsDirty = true
                callback.succeed(data)
            }

            override fun failed(log: String) {
                callback.failed(log)
            }
        }, user)
    }

    override fun updateUserPortrait(callback: GeneralCallback<String>, studentId: String, localFilePath: String) {
        remoteDataSource.updateUserPortrait(object : GeneralCallback<String> {
            override fun succeed(data: String) {
                mCachedUserIsDirty = true
                callback.succeed(data)
            }

            override fun failed(log: String) {
                callback.failed(log)
            }
        }, studentId, localFilePath)
    }

    /**
     * 还是可以做下当前用户信息的内存缓存
     *
     * @param callback .
     * @param userId   userId
     */
    override fun getUserInfo(callback: GeneralCallback<User>, userId: Int) {
        if (this.userId != userId) {
            remoteDataSource.getUserInfo(callback, userId)
            return
        }

        // 对当前用户做的缓存
        if (mCachedUserIsDirty) {
            remoteDataSource.getUserInfo(object : GeneralCallback<User> {
                override fun succeed(data: User) {
                    mCachedUser = data
                    mCachedUserIsDirty = false
                    callback.succeed(mCachedUser!!)
                }

                override fun failed(log: String) {
                    callback.failed(log)
                }
            }, userId)
        } else {
            callback.succeed(mCachedUser!!)
        }
    }

    fun getUserInfo(userId: Int, isCached: Boolean, callback: GeneralCallback<User>) {
        if (isCached) {
            localDataSource.getUserInfo(callback, userId)
        } else {
            remoteDataSource.getUserInfo(callback, userId)
        }

    }

    override fun getSchoolList(callback: GeneralCallback<List<School>>) {
        remoteDataSource.getSchoolList(callback)
    }

}
