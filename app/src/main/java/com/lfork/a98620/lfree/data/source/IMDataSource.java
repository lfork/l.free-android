package com.lfork.a98620.lfree.data.source;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.message.Message;
import com.lfork.a98620.lfree.data.entity.message.MessageContentType;

import java.util.List;

/**
 * Created by 98620 on 2018/4/20.
 */
public interface IMDataSource extends DataSource {

    /**
     * 首次打卡界面的时候，加载已有的Message list。
     * @param id  用户的id ， Group的ID， 或是系统的ID
     * @param type {@link MessageContentType}
     * @param callback 回调
     */
    void getMessages(int id, MessageContentType type, GeneralCallback<List<Message>> callback);

    /**
     * 将最新的一条消息推送到view界面 ，
     */
//    void setViewReference(ChatWindowContract.View view);

    void dealCommand();

    void dealNotification();

    //IM模块直接运行在Service里面


    /**
     * 收到消息后
     * 1、程序在后台运行：进行Notification的通知
     * 2、程序在前台运行，非消息列表fragment。进行notification的通知
     * 3、程序在前台运行，消息列表。不进行notification的通知。直接在消息列表显示未读数量
     * 4、程序在消息窗口运行，当前联系人。直接进行消息的推送
     * 5、程序在消息窗口运行，非当前联系人。进行notification的通知
     */
    void pushMessage();

    /**
     * 前台把数据储存到消息仓库和消息队列， 然后再由信使从消息仓库拿走消息
     * local 负责储存本地消息  remote负责把消息发送到服务器
     * @param msg 需要发送的消息
     * @param callback 回调
     */
    void saveAndSendMessage(Message msg,  GeneralCallback<Message> callback);


    /**
     *
     * @param id  用户的id ， Group的ID， 或是系统的ID
     * @param type {@link MessageContentType}
     */
    void clearMessages(int id, MessageContentType type);
}
