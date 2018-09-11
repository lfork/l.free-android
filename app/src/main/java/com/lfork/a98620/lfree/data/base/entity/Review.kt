package com.lfork.a98620.lfree.data.base.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *
 * Created by 98620 on 2018/8/29.
 */
class Review(@field:SerializedName("rContent")
             var content: String?) : Serializable {

    @SerializedName("gId")
    var goodsId: String? = null

    var user: User? = null

    var userId: String? = null

    @SerializedName("rMakeDate")
    var time: String? = null

    @SerializedName("rId")
    var id: Int = 0

}
