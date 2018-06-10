package com.lfork.a98620.lfree.goodsupload;

/**
 * Created by 98620 on 2018/5/28.
 */
public interface GoodsUploadNavigator {
    void uploadSucceed();

    void uploadFailed(String log);

    void deleteImage(int index);

    void showMyDialog(int count);

}
