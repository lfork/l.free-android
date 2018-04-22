package com.lfork.a98620.lfree.data.source.local;

import android.view.View;

import com.lfork.a98620.lfree.data.source.MessageDataSource;
import com.lfork.a98620.lfree.data.entity.message.Message;
import com.lfork.a98620.lfree.data.entity.message.MessageContentType;

import java.util.List;

public class MessageLocalDataSource implements MessageDataSource {

    private static  MessageLocalDataSource INSTANCE;

    public static MessageLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageLocalDataSource();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getMessages(int id, MessageContentType type, GeneralCallback<List<Message>> callback) {
            callback.succeed(null);
    }

    @Override
    public void setViewReference(View view) {

    }

    @Override
    public void dealCommand() {

    }

    @Override
    public void dealNotification() {

    }

    @Override
    public void saveAndSendMessage(Message msg, GeneralCallback<Message> callback) {
        callback.succeed(null);
    }

    @Override
    public void clearMessages(int id, MessageContentType type) {

    }

    @Override
    public void addMessage(Message msg) {

    }
}
