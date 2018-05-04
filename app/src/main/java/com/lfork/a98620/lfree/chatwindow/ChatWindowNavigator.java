package com.lfork.a98620.lfree.chatwindow;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.imservice.message.Message;

/**
 * Created by 98620 on 2018/4/22.
 */
public interface ChatWindowNavigator {
    void sendMessage(Message message, DataSource.GeneralCallback<Message> callback);


    void showToast(String content);

    void notifyMessagesChanged();
}
