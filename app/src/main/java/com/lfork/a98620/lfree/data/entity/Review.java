package com.lfork.a98620.lfree.data.entity;

/**
 * Created by 98620 on 2018/5/7.
 */
public class Review  {
   private String content;

   private String goodsId;

   private String userId;

    public Review(String content) {
        this.content = content;
    }

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

    //    public
}
