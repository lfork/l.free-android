package com.lfork.a98620.lfree.data.imdata;


import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.imservice.MessageListener;
import com.lfork.a98620.lfree.imservice.message.Message;
import com.lfork.a98620.lfree.imservice.message.MessageContentType;

import java.util.List;

/**
 *  【消息的处理】
 *
 *  消息发送前的打包
 *
 *  消息的发送
 *
 *  服务器对消息的临时储存(只要客户端成功接收消息后 就删除服务端的消息)
 *
 *  消息的接收
 *
 *  消息的本地保存
 *
 *  消息接收后的解析
 *
 *  消息解析后的处理
 *
 */

/* 【消息的类型】：聊天消息、告知型通知、命令型通知(用户可见)、命令型通知(用户不可见)
 * 用户-系统-用户 用户之间的聊天消息：文本与图片
 *
 * 系统-用户 提示型通知：上次登录地点、欢迎使用等。。。
 *
 * 用户-系统-用户 命令型通知：添加好友的请求、邀请你加入群聊、群聊已解散
 *      添加好友(TCP与UDP同时进行)： A编辑给B的验证信息->A将请求发送到服务端->服务端再发送给B->
 *        B接收后作出判断->B将判断结果发给服务端->
 *        服务端将添加结果进行处理：
 *        如果B同意了请求:那么服务端将A和B添加为好友。 然后发送一个刷新好友列表的命令给B，
 *        再发送一个附带有好友添加成功信息的刷新好友列表的命令给A  -> A、B接收到消息后再对好友列表进行刷新
 *
 *        如果B拒接了请求:那么服务端就发送一条通知消息给A
 */


public interface MessageDataSource extends DataSource {

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
    void setMessageListener(MessageListener listener);




    /**
     * 前台把数据储存到消息仓库和消息队列， 然后再由信使从消息仓库拿走消息
     * local 负责储存本地消息  remote负责把消息发送到服务器
     * @param msg 需要发送的消息
     * @param callback 回调
     */
    void saveAndSendMessage(Message msg, GeneralCallback<Message> callback);


    /**
     * 删除消息记录
     * @param id  用户的id ， Group的ID， 或是系统的ID
     * @param type {@link MessageContentType}
     */
    void clearMessages(int id, MessageContentType type);


    //消息队列与消息仓库



}
