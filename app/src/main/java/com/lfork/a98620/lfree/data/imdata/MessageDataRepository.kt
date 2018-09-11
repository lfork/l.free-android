package com.lfork.a98620.lfree.data.imdata

import android.util.Log
import com.lfork.a98620.lfree.base.FreeApplication
import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.imdata.local.MessageLocalDataSource
import com.lfork.a98620.lfree.data.imdata.remote.MessageRemoteDataSource
import com.lfork.a98620.lfree.imservice.MessageListener
import com.lfork.a98620.lfree.imservice.message.Message
import com.lfork.a98620.lfree.imservice.message.MessageContentType
import com.lfork.a98620.lfree.imservice.message.MessageStatus
import java.util.*

/**
 *
 * Created by 98620 on 2018/9/3.
 */

class MessageDataRepository private constructor(private val mMessageRemoteDataSource: MessageRemoteDataSource, private val mMessageLocalDataSource: MessageLocalDataSource) : MessageDataSource, MessageListener {


    //每个联系人之间是有消息的, 每个群组也是有消息的
    private val mCachedUserMessages: HashMap<String, List<Message>> = HashMap()    //key 为friend 和 Group的ID , mCachedGroupMessages

    private var mCachedUserMessagesIsDirty: Boolean = false//, mCachedGroupMessagesIsDirty;

    private var listener: MessageListener? = null

    private val messageQueueOfServer: List<Message>? = null   //****来自服务端的消息的队列，发送给服务端的消息不需要消息队列****

    companion object {
        private var INSTANCE: MessageDataRepository? = null

        fun getInstance(userId: Int): MessageDataRepository {
            if (INSTANCE != null) {
                return INSTANCE as MessageDataRepository
            }
            INSTANCE = MessageDataRepository(MessageRemoteDataSource.getInstance(), MessageLocalDataSource.getInstance())
            INSTANCE!!.mMessageRemoteDataSource.setRepository(INSTANCE!!)
            return INSTANCE as MessageDataRepository
        }

        fun destroyInstance() {
            MessageRemoteDataSource.destroyInstance()
            MessageLocalDataSource.destroyInstance()
            INSTANCE = null
        }
    }


    /**
     * @param msg mRemoteDataSource 传过来的最新的一条或者是多条消息
     */
    fun refreshLocalMessageData(msg: String) {
        //这里还是需要对Message直接通过字符串来进行类型判断 然后再做相应的处理
        //        mCachedUserMessages.put()
    }

    /**
     * 这个操作不会对消息进行及时刷新，只是对已有的消息进行获取
     *
     * @param id       用户的id ， Group的ID， 或是系统的ID
     * @param type     [MessageContentType]
     * @param callback 回调
     */
    override fun getMessages(id: Int, type: MessageContentType, callback: GeneralCallback<List<Message>>) {
        val messageList = mCachedUserMessages[id.toString() + ""]
        if (messageList != null && !mCachedUserMessagesIsDirty) {
            callback.succeed(messageList)
            return
        }

        mMessageLocalDataSource.getMessages(id, type, object : GeneralCallback<List<Message>> {
            override fun succeed(data: List<Message>) {
                mCachedUserMessagesIsDirty = false
                mCachedUserMessages[id.toString() + ""] = data
                callback.succeed(data)
            }

            override fun failed(log: String) {
                callback.failed(log)
            }
        })
    }


    override fun saveAndSendMessage(msg: Message, callback: GeneralCallback<Message>) {
        //将消息进行缓存
        when (msg.contentType) {

        }//                mCachedUserMessages.get(msg.getReceiverID()).add(msg);

        //将消息进行本地储存
        mMessageLocalDataSource.saveAndSendMessage(msg, object : GeneralCallback<Message> {
            override fun succeed(data: Message) {
                saveMessageCache(msg)
                //将消息发送到服务器(消息本地储存成功之后才发送到服务器)
                mMessageRemoteDataSource.saveAndSendMessage(msg, object : GeneralCallback<Message> {
                    override fun succeed(data: Message) {
                        data.status = MessageStatus.SENT
                        callback.succeed(data)
                    }

                    override fun failed(log: String) {
                        callback.failed(log)
                    }
                })
            }

            override fun failed(log: String) {
                callback.failed(log)
            }
        })    //将消息保存到本地

    }

    override fun clearMessages(id: Int, type: MessageContentType) {}

    /**
     * 将消息进行缓存
     *
     * @param msg e
     */
    private fun saveMessageCache(msg: Message) {

        FreeApplication.executeThreadInDefaultThreadPool {
            var messageList: MutableList<Message>? = mCachedUserMessages[msg.receiverID.toString() + ""] as MutableList<Message>
            if (messageList == null) {
                messageList = ArrayList()
                mCachedUserMessages[msg.receiverID.toString() + ""] = messageList
            }
            messageList.add(msg)
        }

    }


    private fun addReceivedMessage(msg: Message) {

        FreeApplication.executeThreadInDefaultThreadPool {
            var messageList: MutableList<Message>? = mCachedUserMessages[msg.senderID.toString() + ""] as MutableList<Message>
            if (messageList == null) {
                messageList = ArrayList()
                mCachedUserMessages[msg.senderID.toString() + ""] = messageList
            }
            messageList.add(msg)
        }


    }

    override fun setMessageListener(listener: MessageListener) {
        this.listener = listener
        mMessageRemoteDataSource.setMessageListener(this)
    }

    //    /**
    //     * 收到消息后
    //     * 1、程序在后台运行：进行Notification的通知
    //     * 2、程序在前台运行，非消息列表fragment。进行notification的通知
    //     * 3、程序在前台运行，消息列表。不进行notification的通知。直接在消息列表显示未读数量
    //     * 4、程序在消息窗口运行，当前联系人。直接进行消息的推送
    //     * 5、程序在消息窗口运行，非当前联系人。进行notification的通知
    //     */
    override fun onReceived(message: Message) {
        message.chatType = Message.ReceiveType
        mMessageLocalDataSource.saveAndSendMessage(message, object : GeneralCallback<Message> {
            override fun succeed(data: Message) {
                Log.d("MessageDataRepository", "succeed: 消息储存成功")
                addReceivedMessage(message)
                mCachedUserMessagesIsDirty = true
                listener!!.onReceived(message)
            }

            override fun failed(log: String) {
                Log.d("MessageDataRepository", "failed: $log")
            }
        })
    }


}
