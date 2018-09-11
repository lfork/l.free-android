package com.lfork.a98620.lfree.data.base.entity

/**
 *
 * Created by 98620 on 2018/8/29.
 */
class School(val id: String, val schoolName: String = "??") {
    override fun toString(): String {
        return schoolName
    }
}