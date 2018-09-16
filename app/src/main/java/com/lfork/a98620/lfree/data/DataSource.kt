package com.lfork.a98620.lfree.data

/**
 *
 * Created by 98620 on 2018/8/30.
 */
interface DataSource {
    interface GeneralCallback<T> {
        fun succeed(data: T)
        fun failed(log: String)
    }
}