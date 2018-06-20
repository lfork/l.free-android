package com.lfork.a98620.lfree.data.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class Goods implements Serializable{

    @SerializedName("gName")
    private String name;

    @SerializedName("gId")
    private int id;

    @SerializedName("gBuyPrice")
    private String originPrice;

    @SerializedName("gSellPrice")
    private String price;

    @SerializedName("gCoverImage")
    private String coverImagePath;

    private String[] imagesPath;

    @SerializedName("gDesc")
    private String description;

    @SerializedName("userImage")
    private String userPortraitPath;

    @SerializedName("gMakeDate")
    private String publishDate;

    private int userId;

    private int categoryId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserPortraitPath() {
        return userPortraitPath;
    }

    public void setUserPortraitPath(String userPortraitPath) {
        this.userPortraitPath = userPortraitPath;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String[] getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String[] imagesPath) {
        this.imagesPath = imagesPath;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {

        return "【商品名称】:" + getName() +
                "\n【商品价格】：" + getPrice() +
                "\n【描述】:" + getDescription()+
                "\n更多详细信息，就赶快来下载L.Free吧 "+
                "\nhttp://www.lfork.top";
    }
}
