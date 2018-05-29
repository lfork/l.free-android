package com.lfork.a98620.lfree.data.imdata;


import android.util.Log;

import com.lfork.a98620.lfree.data.imdata.local.MessageLocalDataSource;
import com.lfork.a98620.lfree.data.imdata.remote.MessageRemoteDataSource;
import com.lfork.a98620.lfree.imservice.MessageListener;
import com.lfork.a98620.lfree.imservice.message.Message;
import com.lfork.a98620.lfree.imservice.message.MessageContentType;
import com.lfork.a98620.lfree.imservice.message.MessageStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageDataRepository<C> implements MessageDataSource, MessageListener {
    //每个联系人之间是有消息的, 每个群组也是有消息的
    private HashMap<String, List<Message>> mCachedUserMessages;    //key 为friend 和 Group的ID , mCachedGroupMessages

    private boolean mCachedUserMessagesIsDirty;//, mCachedGroupMessagesIsDirty;

    private MessageRemoteDataSource mMessageRemoteDataSource;

    private MessageLocalDataSource mMessageLocalDataSource;

    private MessageListener listener;

    private List<Message> messageQueueOfServer;   //****来自服务端的消息的队列，发送给服务端的消息不需要消息队列****


    private static MessageDataRepository INSTANCE;

    private MessageDataRepository(MessageRemoteDataSource mMessageRemoteDataSource, MessageLocalDataSource mMessageLocalDataSource) {
        this.mMessageRemoteDataSource = mMessageRemoteDataSource;
        this.mMessageLocalDataSource = mMessageLocalDataSource;

        mCachedUserMessages = new HashMap<>();
    }

    public static MessageDataRepository getInstance(int userId) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new MessageDataRepository(MessageRemoteDataSource.getInstance(userId), MessageLocalDataSource.getInstance());
        INSTANCE.mMessageRemoteDataSource.setRepository(INSTANCE);
        return INSTANCE;
    }

    public static void destroyInstance() {
        MessageRemoteDataSource.destroyInstance();
        MessageLocalDataSource.destroyInstance();
        INSTANCE = null;
    }

    /**
     * @param msg mRemoteDataSource 传过来的最新的一条或者是多条消息
     */
    public void refreshLocalMessageData(String msg) {
        //这里还是需要对Message直接通过字符串来进行类型判断 然后再做相应的处理
//        mCachedUserMessages.put()
    }

    /**
     * 这个操作不会对消息进行及时刷新，只是对已有的消息进行获取
     *
     * @param id       用户的id ， Group的ID， 或是系统的ID
     * @param type     {@link MessageContentType}
     * @param callback 回调
     */
    @Override
    public void getMessages(int id, MessageContentType type, GeneralCallback<List<Message>> callback) {
        List<Message> messageList = mCachedUserMessages.get(id + "");
        if (messageList != null && !mCachedUserMessagesIsDirty) {
            callback.succeed(messageList);
            return;
        }

        mMessageLocalDataSource.getMessages(id, type, new GeneralCallback<List<Message>>() {
            @Override
            public void succeed(List<Message> data) {
                mCachedUserMessagesIsDirty = false;
                mCachedUserMessages.put(id + "", data);
                callback.succeed(data);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);
            }
        });
    }


    @Override
    public void saveAndSendMessage(Message msg, GeneralCallback<Message> callback) {
        //将消息进行缓存
        switch (msg.getContentType()) {
            case COMMAND:
            case NOTIFICATION:
            case COMMUNICATION_GROUP:
            case COMMUNICATION_USER:
//                mCachedUserMessages.get(msg.getReceiverID()).add(msg);
        }

        //将消息进行本地储存
        mMessageLocalDataSource.saveAndSendMessage(msg, new GeneralCallback<Message>() {
            @Override
            public void succeed(Message data) {
                saveMessageCache(msg);
                //将消息发送到服务器(消息本地储存成功之后才发送到服务器)
                mMessageRemoteDataSource.saveAndSendMessage(msg, new GeneralCallback<Message>() {
                    @Override
                    public void succeed(Message data) {
                        data.setStatus(MessageStatus.SENT);
                        callback.succeed(data);
                    }

                    @Override
                    public void failed(String log) {
                        callback.failed(log);
                    }
                });
            }

            @Override
            public void failed(String log) {
                callback.failed(log);
            }
        });    //将消息保存到本地

    }

    @Override
    public void clearMessages(int id, MessageContentType type) {
    }

    /**
     * 将消息进行缓存
     *
     * @param msg
     */
    private void saveMessageCache(Message msg) {
        new Thread(() -> {
            List<Message> messageList = mCachedUserMessages.get(msg.getReceiverID() + "");
            if (messageList == null) {
                messageList = new ArrayList<>();
                mCachedUserMessages.put(msg.getReceiverID() + "", messageList);
            }
            messageList.add(msg);
        }).start();

    }


    public void addReceivedMessage(Message msg) {
        msg.setChatType(Message.ReceiveType);
        new Thread(() -> {
            List<Message> messageList = mCachedUserMessages.get(msg.getSenderID() + "");
            if (messageList == null) {
                messageList = new ArrayList<>();
                mCachedUserMessages.put(msg.getSenderID() + "", messageList);
            }
            messageList.add(msg);
        }).start();

    }

    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
        mMessageRemoteDataSource.setMessageListener(this);
    }

    //    /**
//     * 收到消息后
//     * 1、程序在后台运行：进行Notification的通知
//     * 2、程序在前台运行，非消息列表fragment。进行notification的通知
//     * 3、程序在前台运行，消息列表。不进行notification的通知。直接在消息列表显示未读数量
//     * 4、程序在消息窗口运行，当前联系人。直接进行消息的推送
//     * 5、程序在消息窗口运行，非当前联系人。进行notification的通知
//     */
    @Override
    public void onReceived(Message message) {
        mMessageLocalDataSource.saveAndSendMessage(message, new GeneralCallback<Message>() {
            @Override
            public void succeed(Message data) {
                Log.d("MessageDataRepository", "succeed: 消息储存成功");
                addReceivedMessage(message);
                mCachedUserMessagesIsDirty = true;
                listener.onReceived(message);
            }

            @Override
            public void failed(String log) {
                Log.d("MessageDataRepository", "failed: " + log);
            }
        });
    }
}
