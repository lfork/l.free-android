package com.lfork.a98620.lfree.data.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 98620 on 2018/5/7.
 */
public class Review implements Serializable {


    @SerializedName("rContent")
    private String content;


    @SerializedName("rId")
    private String goodsId;

    private User user;

    private String userId;

    @SerializedName("rMakeDate")
    private String time;

    public Review(String content) {
        this.content = content;
    }

    @SerializedName("gId")
    private int id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //    public
}
