package com.lfork.a98620.lfree.goodsuploadupdate;

import android.net.Uri;

import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;

import java.util.List;

/**
 * Created by 98620 on 2018/5/28.
 */
public interface GoodsUploadUpdateNavigator extends ViewModelNavigator {
    void uploadSucceed(String msg);

    void uploadFailed(String log);

    void deleteImage(int index);

    void showMyDialog(int count);

    void setDefaultCategory(int category);

    void setImages(List<Uri> images);


}
