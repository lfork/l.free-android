package com.lfork.a98620.lfree.data.base.entity

import org.litepal.crud.DataSupport
import java.io.Serializable

/**
 * Created by 98620 on 2018/4/7.
 */

data class Category(val id: Int, val csId: Int, var csName: String?) : DataSupport(), Serializable