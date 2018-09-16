package com.lfork.a98620.lfree.data.base.entity

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 *
 * Created by 98620 on 2018/8/29.
 */

class GoodsDetailInfo : Goods() {

    @SerializedName("username")
    val sellerName: String? = null

    val images: Array<String>? = null

    val reviews: ArrayList<Review>? = null

}

