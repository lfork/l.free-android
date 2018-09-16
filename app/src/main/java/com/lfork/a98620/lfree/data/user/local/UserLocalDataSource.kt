package com.lfork.a98620.lfree.data.user.local

import android.util.Log
import com.lfork.a98620.lfree.data.DataSource
import com.lfork.a98620.lfree.data.base.entity.School
import com.lfork.a98620.lfree.data.base.entity.User
import com.lfork.a98620.lfree.data.user.UserDataSource
import org.litepal.crud.DataSupport

/**
 *
 * Created by 98620 on 2018/8/30.
 */
object UserLocalDataSource : UserDataSource {

    private val TAG = "IMRemoteDataSource"

    fun destroyInstance() {
        //do nothing here
    }

    override fun login(callback: DataSource.GeneralCallback<User>, user: User) {}

    override fun register(callback: DataSource.GeneralCallback<String>, user: User) {
        //do nothing here
    }

    fun getThisUser(callback: DataSource.GeneralCallback<User>) {
        val userList = DataSupport.where("isLogin=?", "1").find(User::class.java)
        var user: User? = null
        if (userList != null && userList.size > 0) {
            user = userList[0]
        }

        if (user != null) {
            callback.succeed(user)
        } else {
            callback.failed("没有用户")
        }
    }

    fun saveThisUser(user: User): Boolean {
        try {
            DataSupport.deleteAll(User::class.java, "islogin=0 and userid=? ", user.userId.toString() + "")
            val result = user.save()
            Log.d(TAG, "saveThisUser: 用户信息本地更新成功$result")
            return result
        } catch (e: Exception) {
            return false
        }

    }

    override fun updateUserInfo(callback: DataSource.GeneralCallback<User>, user: User) {

        try {
            DataSupport.deleteAll(User::class.java, "islogin=0 and userid=? ", user.userId.toString() + "")
            val result = user.save()
            Log.d(TAG, "saveThisUser: 用户信息本地更新成功$result")
        } catch (e: Exception) {
        }

        user.update(user.id.toLong())
    }

    override fun updateUserPortrait(callback: DataSource.GeneralCallback<String>, studentId: String, localFilePath: String) {

    }

    override fun getUserInfo(callback: DataSource.GeneralCallback<User>, userId: Int) {
        val list = DataSupport.where("userid=?", userId.toString() + "").find(User::class.java)
        if (list != null && list.size > 0) {
            callback.succeed(list[0])
        } else {
            callback.failed("没有数据")
        }
    }

    override fun getSchoolList(callback: DataSource.GeneralCallback<List<School>>) {
    }
}
