package com.lfork.a98620.lfree.imservice;

///**
// * Created by 98620 on 2017/8/18.
// */

import com.lfork.a98620.lfree.imservice.message.Message;

public interface MessageListener {
    void onReceived(Message message);
//    void onSuccess();
//    void onFailure();
//    void onCanceled();
//    void onPaused();
//    void onProgress(int progress);
}
