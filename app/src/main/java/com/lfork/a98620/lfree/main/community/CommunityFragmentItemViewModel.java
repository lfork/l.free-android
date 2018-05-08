package com.lfork.a98620.lfree.main.community;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;


import com.lfork.a98620.lfree.base.BaseViewModel;

import java.util.List;

public class CommunityFragmentItemViewModel extends BaseViewModel {
    private ObservableField<String> article = new ObservableField<>();
    private ObservableInt articleId = new ObservableInt();
    private ObservableField<String> articleTime = new ObservableField<>();
    private ObservableField<List<String>> imageUriList = new ObservableField<>();
    private ObservableInt publisherId = new ObservableInt();
    private View view;

    public ObservableInt getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(ObservableInt publisherId) {
        this.publisherId = publisherId;
    }

    public ObservableField<String> getArticle() {
        return article;
    }

    public void setArticle(ObservableField<String> article) {
        this.article = article;
    }

    public ObservableInt getArticleId() {
        return articleId;
    }

    public void setArticleId(ObservableInt articleId) {
        this.articleId = articleId;
    }

    public ObservableField<String> getArticleTime() {
        return articleTime;
    }

    public void setArticleTime(ObservableField<String> articleTime) {
        this.articleTime = articleTime;
    }

    public ObservableField<List<String>> getImageUriList() {
        return imageUriList;
    }

    public void setImageUriList(ObservableField<List<String>> imageUriList) {
        this.imageUriList = imageUriList;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void articleOnClick(int articleId) {

    }

    public void headImgAndHeadNameOnClick(int publisherId) {

    }
}
