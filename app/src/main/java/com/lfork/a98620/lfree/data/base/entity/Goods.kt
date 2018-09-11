package com.lfork.a98620.lfree.data.base.entity

import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 *
 * Created by 98620 on 2018/3/31.
 */

open class Goods : Serializable {
    @SerializedName("gName")
    var name: String? = null

    @SerializedName("gId")
    var id: Int = 0

    @SerializedName("gBuyPrice")
    var originPrice: String? = null

    @SerializedName("gSellPrice")
    var price: String? = null

    @SerializedName("gCoverImage")
    var coverImagePath: String? = null

    var imagesPath: Array<String>? = null

    @SerializedName("desc")
    var description: String? = null

    @SerializedName("userImage")
    var userPortraitPath: String? = null

    @SerializedName("gMakeDate")
    var publishDate: String? = null

    var userId: Int = 0

    var categoryId: Int = 0

    override fun toString(): String {
        return "【商品名称】:" + name +
                "\n【商品价格】：" + price +
                "\n【描述】:" + description +
                "\n更多详细信息，就赶快来下载L.Free吧 " +
                "\nhttp://www.lfork.top"
    }
}
