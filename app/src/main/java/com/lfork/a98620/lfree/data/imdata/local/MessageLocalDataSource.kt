package com.lfork.a98620.lfree.data.imdata.local

import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.imdata.MessageDataSource
import com.lfork.a98620.lfree.imservice.MessageListener
import com.lfork.a98620.lfree.imservice.message.Message
import com.lfork.a98620.lfree.imservice.message.MessageContentType
import org.litepal.crud.DataSupport
import org.litepal.crud.callback.FindMultiCallback

/**
 *
 * Created by 98620 on 2018/9/3.
 */
class MessageLocalDataSource : MessageDataSource {

    companion object {
        private var INSTANCE: MessageLocalDataSource? = null
        fun getInstance(): MessageLocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = MessageLocalDataSource()
            }
            return INSTANCE as MessageLocalDataSource
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    override fun getMessages(id: Int, type: MessageContentType, callback: GeneralCallback<List<Message>>) {
        DataSupport.where("receiverid=? or senderid=?", id.toString() + "", id.toString() + "").order("messageID")  //messageId 实际上是message生成的时间 System.currentTimeMillis()
                .findAsync(Message::class.java)
                .listen(object : FindMultiCallback {
                    override fun <T> onFinish(t: List<T>) {

                        if (t.size < 1) {
                            callback.failed("没有消息记录")
                        } else {
                            callback.succeed(t as List<Message>)
                        }
                    }
                })

    }

    override fun setMessageListener(listener: MessageListener) {
        //消息监听交给 remote进行处理
    }

    override fun saveAndSendMessage(msg: Message, callback: GeneralCallback<Message>) {
        msg.saveAsync().listen { success ->
            if (success) {
                callback.succeed(Message())
            } else {
                callback.failed("消息重复")
            }
        }

    }

    override fun clearMessages(id: Int, type: MessageContentType) {

    }


}

