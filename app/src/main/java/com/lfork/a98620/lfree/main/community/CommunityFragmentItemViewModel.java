package com.lfork.a98620.lfree.main.community;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.lfork.a98620.lfree.base.BaseViewModel;

import java.util.List;

public class CommunityFragmentItemViewModel extends BaseViewModel {
    private ObservableField<String> article = new ObservableField<>();  //动态文字内容
    private ObservableInt articleId = new ObservableInt();    //动态id
    private ObservableInt publisherId = new ObservableInt();  //发布人id
    private ObservableField<String> articleTime = new ObservableField<>();  //动态发布时间
    private ObservableField<List<String>> imageUriList = new ObservableField<>();   //动态包含的图片URI链接

    private ObservableField<String> headImgUri = new ObservableField<>();   //发布人头像URI链接
    private ObservableField<String> headName = new ObservableField<>(); //发布人name

    public String getArticle() {
        return article.get();
    }
    public void setArticle(String article) {
        this.article.set(article);
    }

    public int getArticleId() {
        return articleId.get();
    }
    public void setArticleId(int articleId) {
        this.articleId.set(articleId);
    }

    public String getArticleTime() {
        return articleTime.get();
    }
    public void setArticleTime(String articleTime) {
        this.articleTime.set(articleTime);
    }

    public String getHeadImgUri() {
        return headImgUri.get();
    }
    public void setHeadImgUri(String headImgUri) {
        this.headImgUri.set(headImgUri);
    }

    public String getHeadName() {
        return headName.get();
    }

    public void setHeadName(String headName) {
        this.headName.set(headName);
    }

    public List<String> getImageUriList() {
        return imageUriList.get();
    }

    public void setImageUriList(List<String> imageUriList) {
        this.imageUriList.set(imageUriList);
    }

    public int getPublisherId() {
        return publisherId.get();
    }

    public void setPublisherId(int publisherId) {
        this.publisherId.set(publisherId);
    }
}
