package com.lfork.a98620.lfree.data.imdata.remote

import com.google.gson.reflect.TypeToken
import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.base.entity.IMUser
import com.lfork.a98620.lfree.data.base.entity.User
import com.lfork.a98620.lfree.data.imdata.IMDataSource
import com.lfork.a98620.lfree.imservice.Config
import com.lfork.a98620.lfree.imservice.TCPConnection
import com.lfork.a98620.lfree.imservice.request.LoginListener
import com.lfork.a98620.lfree.imservice.request.Request
import com.lfork.a98620.lfree.imservice.request.Result
import com.lfork.a98620.lfree.imservice.request.UserRequestType
import com.lfork.a98620.lfree.util.JSONUtil

/**
 *
 * Created by 98620 on 2018/9/3.
 */
class IMRemoteDataSource : IMDataSource {
    /**
     * 伪单例、不需要线程同步、懒汉模式
     */
    companion object {
        private var INSTANCE: IMRemoteDataSource? = null
        fun getInstance(): IMRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = IMRemoteDataSource()
            }
            return INSTANCE as IMRemoteDataSource
        }

        fun destroyInstance() {
            INSTANCE?.mConnection?.closeConnection()
            INSTANCE = null
        }
    }

    private var mConnection: TCPConnection = TCPConnection(Config.URL, 7000)

    init{
        mConnection.start()
    }

    override fun login(user: User, listener: LoginListener) {
        if (!mConnection.isRunning){
            mConnection.start()
        }

        val u = IMUser(user.id)
        val request = Request<IMUser>()
                .setData(u)
                .setRequestType(UserRequestType.DO_LOGIN)
                .setMessage("haha")

        mConnection.setUser(u)

        if (!mConnection.send(request.request)) {
            listener.failed("与服务器的连接已断开")
            return
        }
        //这里还需要添加一个超时检测， 因为网络连接有问题的话 服务器是不会发送回执给客户端的
        //超时检测线程  -》 错了，应该在TCPConnection里面设置这个，

        val strResult = mConnection.receive()
        val result = JSONUtil.parseJson<Result<User>>(strResult, object : TypeToken<Result<User>>() {

        })
        if (result != null) {
            when (result.code) {
                -1 -> listener.failed("未知错误 -1")
                0 -> listener.failed("密码错误")
                1 -> {
                    listener.succeed(result.data)
                }
                2 -> listener.failed("账号无效")
                else -> listener.failed("未知错误 -2")
            }
        } else {
            listener.failed("连接失败")
        }


    }

    override fun logout(userId: Int, callback: GeneralCallback<String>) {
        val request = Request<String>()
                .setRequestType(UserRequestType.DO_LOGOUT)
                .setMessage(userId.toString() + "")

        if (!mConnection.send(request.getRequest())) {
            callback.failed("与服务器的连接已断开")
            return
        }

        val result = JSONUtil.parseJson<Result<*>>(mConnection.receive(), object : TypeToken<Result<*>>() {

        })
        if (result != null) {
            when (result.code) {
                0 -> callback.failed("未知错误 -3")
                1 -> callback.succeed("登录成功 1")
                else -> callback.failed("未知错误 -2")
            }
        } else {
            callback.failed("连接失败")
        }
    }

    override fun getChatUserList(chatUserList: GeneralCallback<List<User>>) {
    }

    override fun addChatUser(user: User, callback: GeneralCallback<String>) {
    }

    override fun addChatUser(user: User, isExisted: Boolean, callback: GeneralCallback<String>) {
    }

    override fun removeChatUser(userId: Int, callback: GeneralCallback<List<User>>) {
    }
}
