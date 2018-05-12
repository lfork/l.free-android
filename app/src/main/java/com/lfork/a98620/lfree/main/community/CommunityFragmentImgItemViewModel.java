package com.lfork.a98620.lfree.main.community;

import com.lfork.a98620.lfree.base.BaseViewModel;

public class CommunityFragmentImgItemViewModel extends BaseViewModel {
    private String imgUri;

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public CommunityFragmentImgItemViewModel(String imgUri) {
        this.imgUri = imgUri;
    }
}
