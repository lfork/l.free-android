package com.lfork.a98620.lfree.data.imdata

import com.lfork.a98620.lfree.data.DataSource
import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.imservice.MessageListener
import com.lfork.a98620.lfree.imservice.message.Message
import com.lfork.a98620.lfree.imservice.message.MessageContentType

/**
 *
 * Created by 98620 on 2018/9/3.
 */
interface MessageDataSource : DataSource {

    /**
     * 首次打开界面的时候，加载已有的Message list。
     * @param id  用户的id ， Group的ID， 或是系统的ID
     * @param type [MessageContentType]
     * @param callback 回调
     */
    fun getMessages(id: Int, type: MessageContentType, callback: GeneralCallback<List<Message>>)

    /**
     * 将最新的一条消息推送到view界面 ，
     */
    fun setMessageListener(listener: MessageListener)


    /**
     * 前台把数据储存到消息仓库和消息队列， 然后再由信使从消息仓库拿走消息
     * local 负责储存本地消息  remote负责把消息发送到服务器
     * @param msg 需要发送的消息
     * @param callback 回调
     */
    fun saveAndSendMessage(msg: Message, callback: GeneralCallback<Message>)


    /**
     * 删除消息记录
     * @param id  用户的id ， Group的ID， 或是系统的ID
     * @param type [MessageContentType]
     */
    fun clearMessages(id: Int, type: MessageContentType)

    //消息队列与消息仓库


}
