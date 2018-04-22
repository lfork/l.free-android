package com.lfork.a98620.lfree.data.source;


import android.view.View;

import com.lfork.a98620.lfree.data.entity.message.Message;
import com.lfork.a98620.lfree.data.entity.message.MessageContentType;
import com.lfork.a98620.lfree.data.entity.message.MessageStatus;
import com.lfork.a98620.lfree.data.source.local.MessageLocalDataSource;
import com.lfork.a98620.lfree.data.source.remote.MessageRemoteDataSource;
import com.lfork.a98620.lfree.data.source.remote.imservice.MessageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageDataRepository<C> implements MessageDataSource {
    //每个联系人之间是有消息的, 每个群组也是有消息的
    private HashMap<String, List<Message>> mCachedUserMessages;    //key 为friend 和 Group的ID , mCachedGroupMessages

    private boolean mCachedUserMessagesIsDirty;//, mCachedGroupMessagesIsDirty;

    private ChatWindowView view;//viewModelOrPresenter;    //接收到消息的时候就需要消息界面进行刷新了

    private MessageRemoteDataSource mMessageRemoteDataSource;

    private MessageLocalDataSource mMessageLocalDataSource;

    private List<Message> messageQueueOfServer;   //****来自服务端的消息的队列，发送给服务端的消息不需要消息队列****

//    private C mContext;         //为Android准备的Context 备不时之需

    private static MessageDataRepository INSTANCE;

    private MessageDataRepository(MessageRemoteDataSource mMessageRemoteDataSource, MessageLocalDataSource mMessageLocalDataSource) {
        this.mMessageRemoteDataSource = mMessageRemoteDataSource;
        this.mMessageLocalDataSource = mMessageLocalDataSource;
//        this.mContext = mContext;

        mCachedUserMessages = new HashMap<>();
//        mCachedGroupMessages = new HashMap<>();
    }

    public static MessageDataRepository getInstance(int userId) {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new MessageDataRepository( MessageRemoteDataSource.getInstance(userId), MessageLocalDataSource.getInstance());
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
        List<Message> messageList =mCachedUserMessages.get(id+"");
        if (messageList != null) {
            callback.succeed(messageList);
            return;
        }

        mMessageLocalDataSource.getMessages(id, type, new GeneralCallback<List<Message>>() {
            @Override
            public void succeed(List<Message> data) {
                data = initMeg();
                mCachedUserMessages.put(id+"", data);
                callback.succeed(data);
            }

            @Override
            public void failed(String log) {
                callback.failed(log);
            }
        });
    }


    private List<Message> initMeg() {
        List<Message> messageList = new ArrayList<>();
        Message apple = new Message("Hey~, guy", Message.SendType);
        messageList.add(apple);
        Message banana = new Message("Hello, who is that?", Message.ReceiveType);
        messageList.add(banana);
        Message orange = new Message("I am King.", Message.SendType);
        messageList.add(orange);
        Message watermelon = new Message("Ok ok , nice to call you", Message.ReceiveType);
        messageList.add(watermelon);

        return messageList;

    }

    @Override
    public void setViewReference(View view) {
        mMessageRemoteDataSource.setViewReference(view);
    }


    @Override
    public void dealCommand() {

    }

    @Override
    public void dealNotification() {

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

                addMessage(msg);
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

    @Override
    public void addMessage(Message msg) {
        new Thread(() -> {
            List<Message> messageList =mCachedUserMessages.get(msg.getReceiverID()+"");
            if (messageList == null) {
                messageList = new ArrayList<>();
                mCachedUserMessages.put(msg.getReceiverID()+"", messageList);
            }

            messageList.add(msg);
        }).start();

    }


    public void addReceivedMessage(Message msg){
        msg.setChatType(Message.ReceiveType);
        new Thread(() -> {
            List<Message> messageList =mCachedUserMessages.get(msg.getSenderID()+"");
            if (messageList == null) {
                messageList = new ArrayList<>();
                mCachedUserMessages.put(msg.getSenderID()+"", messageList);
            }

            messageList.add(msg);
        }).start();

    }

    public void saveRecord(String recordID, Message message) {
    }

    public List<Message> getMessageQueueOfServer() {
        return messageQueueOfServer;
    }


    public void setMessageListener(MessageListener listener){
        mMessageRemoteDataSource.setMessageListener(listener);
    }
}
