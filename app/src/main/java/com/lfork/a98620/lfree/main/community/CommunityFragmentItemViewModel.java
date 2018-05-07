package com.lfork.a98620.lfree.main.community;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;


import com.lfork.a98620.lfree.base.BaseViewModel;

import java.util.List;

public class CommunityFragmentItemViewModel extends BaseViewModel {
    public ObservableField<String> article = new ObservableField<>();
    public ObservableInt articleId = new ObservableInt();
    public ObservableField<String> articleTime = new ObservableField<>();
    public ObservableField<List<String>> imageUriList = new ObservableField<>();

    public CommunityFragmentItemViewModel(String article, int articleId, String articleTime, List<String> imageUriList) {
        this.article.set(article);
        this.articleId.set(articleId);
        this.articleTime.set(articleTime);
        this.imageUriList.set(imageUriList);
    }
}
