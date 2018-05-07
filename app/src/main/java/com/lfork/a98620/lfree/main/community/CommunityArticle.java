package com.lfork.a98620.lfree.main.community;

import java.util.List;

public class CommunityArticle {
    private String article;
    private int articleId;
    private String articleTime;
    private List<String> imageUriList;

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

}

