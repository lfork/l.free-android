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
    private List<String> imageUriList;

    private String headImgUri;
    private String headName;

    private View view;

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
    public String getArticleTime() {
        return articleTime;
    }
    public void setArticleTime(String articleTime) {
        this.articleTime = articleTime;
    }
    public List<String> getImageList() {
        return imageUriList;
    }
    public void setImageList(List<String> imageUriList) {
        this.imageUriList = imageUriList;
    }

    public void setImageUriList(List<String> imageUriList) {
        this.imageUriList = imageUriList;
    }

    public List<String> getImageUriList() {
        return imageUriList;
    }

    public String getHeadImgUri() {
        return headImgUri;
    }

    public void setHeadImgUri(String headImgUri) {
        this.headImgUri = headImgUri;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public void articleOnClick(int articleId) {
        Intent intent = new Intent(view.getContext(), CommentActivity.class);
        intent.putExtra("articleId", articleId);
        view.getContext().startActivity(intent);
    }

    public void headImgAndHeadNameOnClick(int publisherId) {
        Intent intent = new Intent(view.getContext(), UserInfoActivity.class);
        intent.putExtra("user_id", publisherId);
        view.getContext().startActivity(intent);
    }
}

