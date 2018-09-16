package com.lfork.a98620.lfree.data.base.entity

import org.litepal.annotation.Column
import org.litepal.crud.DataSupport

/**
 *
 * Created by 98620 on 2018/8/29.
 */
class User : DataSupport {
    var id: Int = 0 //储存在本地数据库里面的id

    @Column(unique = true, defaultValue = "unknown") //啊啊啊
    var userId: Int = 0     //用户的ID

    var studentId: String? = null  //userId == studentId

    var userName: String? = null    //用户姓名

    var userPassword: String? = null  //用户密码

    var userEmail: String? = null     //用户邮箱地址

    var userPhone: String? = null      //用户电话号码

    var userAddress: String? = null    //用户的住址

    var userImagePath: String? = null   //用户头像文件存放路径

    var userDesc: String? = null          //用户描述

    var userMakeDate: String? = null        //用户修改或者注册时间

    var userSchool: School? = null

    var timestamp: Long = 0

    var isLogin: Boolean = false

    var isChatUser: Boolean = false

    constructor() {}

    /**
     * 注册的时候用
     */
    constructor(userName: String, userPassword: String, userEmail: String, userPhone: String, userAddress: String,
                userImagePath: String, userDesc: String, userMakeDate: String) {
        this.userName = userName
        this.userPassword = userPassword
        this.userEmail = userEmail
        this.userPhone = userPhone
        this.userAddress = userAddress
        this.userImagePath = userImagePath
        this.userDesc = userDesc
        this.userMakeDate = userMakeDate
    }

    override fun toString(): String {
        return ("User [userId=" + userId + ", userName=" + userName + ", userPassword=" + userPassword + ", userEmail="
                + userEmail + ", userPhone=" + userPhone + ", userAddress=" + userAddress + ", userImagePath="
                + userImagePath + ", userDesc=" + userDesc + ", userMakeDate=" + userMakeDate + "]")
    }
}
