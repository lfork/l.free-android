package com.lfork.a98620.lfree.main.community;

import android.content.Intent;
import android.view.View;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;

import java.util.List;

public class CommunityArticle extends BaseViewModel {
    private String article;
    private int articleId;
    private int publisherId;  //发布人id
    private String articleTime;
    private List<CommunityFragmentImgItemViewModel> imageUriList;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public String getArticleTime() {
        return articleTime;
    }

    public void setArticleTime(String articleTime) {
        this.articleTime = articleTime;
    }

    public List<CommunityFragmentImgItemViewModel> getImageUriList() {
        return imageUriList;
    }

    public void setImageUriList(List<CommunityFragmentImgItemViewModel> imageUriList) {
        this.imageUriList = imageUriList;
    }
}

