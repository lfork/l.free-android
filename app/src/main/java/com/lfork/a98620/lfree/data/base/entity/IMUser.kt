package com.lfork.a98620.lfree.data.base.entity

/**
 *
 * Created by 98620 on 2018/8/29.
 */
class IMUser(val id: Int) {
    var username: String = ""
    var password: String = ""
    override fun toString(): String {
        return "[{\"username\":\"$username\",\"id\":\"$id\"}]"
    }
}
