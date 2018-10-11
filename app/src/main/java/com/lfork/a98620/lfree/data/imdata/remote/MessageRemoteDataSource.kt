package com.lfork.a98620.lfree.data.imdata.remote

import android.util.Log
import com.lfork.a98620.lfree.base.FreeApplication
import com.lfork.a98620.lfree.data.DataSource.GeneralCallback
import com.lfork.a98620.lfree.data.imdata.MessageDataRepository
import com.lfork.a98620.lfree.data.imdata.MessageDataSource
import com.lfork.a98620.lfree.data.user.UserDataRepository
import com.lfork.a98620.lfree.imservice.Config
import com.lfork.a98620.lfree.imservice.MessageListener
import com.lfork.a98620.lfree.imservice.UDPConnection
import com.lfork.a98620.lfree.imservice.UDPMessageMaid
import com.lfork.a98620.lfree.imservice.message.Message
import com.lfork.a98620.lfree.imservice.message.MessageContentType
import com.lfork.a98620.lfree.imservice.message.MessageStatus
import java.util.*

/**
 *
 * Created by 98620 on 2018/9/3.
 */

class MessageRemoteDataSource : MessageDataSource {




    //    private List<Message> messageSendQueue;   //发送失败的消息是需要重新发送的。。。  这个交给用户来处理

    private val messageReceiveQueue: List<Message>

    private val mConnection: UDPConnection?  //负责提供UDP的基本服务 + 负责消息的接收和直接储存(内存)

    private val messageMaid: UDPMessageMaid? //负责对直接储存消息的处理，然后进行分类储存(磁盘，数据库)，推送到view界面

    private var mRepository: MessageDataRepository? = null

    //    private View view; //这里还需要一个View的引用和主线程的引用，以便将新消息推送到前台

    var listener: MessageListener? = null
        private set

    init {
        //初始化消息接收队列(储存处)
        messageReceiveQueue = LinkedList()

        //消息接收与储存 + 提供消息发送接口
        mConnection = UDPConnection(UserDataRepository.userId, Config.URL, 7010, messageReceiveQueue)
        mConnection.start()

        //消息处理
        messageMaid = UDPMessageMaid(this, messageReceiveQueue)
        messageMaid.start()
    }


    companion object {
        private var INSTANCE: MessageRemoteDataSource? = null
        fun getInstance(): MessageRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = MessageRemoteDataSource()
            }
            return INSTANCE as MessageRemoteDataSource
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

    fun destroyInstance() {
        messageMaid!!.close()
        mConnection!!.closeConnection()
    }

    fun setRepository(mRepository: MessageDataRepository) {
        this.mRepository = mRepository
    }

    override fun getMessages(id: Int, type: MessageContentType, callback: GeneralCallback<List<Message>>) {}


    /**
     * deal  message
     *
     * @param msg
     */

    fun pushMessage(msg: Message) {
        Log.d("接收到的消息", "pushMessage: $msg")
        if (listener != null) {
            listener!!.onReceived(msg)

        }
    }

    /**
     * 暂时只写发送的操作  也就是说我们不管发送失败的操作，发送失败的消息就现在只能由用户自己重发了
     *
     * @param msg      需要发送的消息
     * @param callback 回调
     */
    @Synchronized
    override fun saveAndSendMessage(msg: Message, callback: GeneralCallback<Message>) {
        FreeApplication.executeThreadInDefaultThreadPool {
            var repeatTimes = 3
            var succeed = false

            while (repeatTimes > 0 && !succeed) {       //这里需要进行3次重发，如果多次发送失败那么最后就交给用户来处理发送失败的信息 2018年3月8日16:28:01
                repeatTimes--

                if (repeatTimes == 1) {
                    mConnection!!.rebuildConnection()
                }
                succeed = mConnection!!.sendNormalMessage(msg)

                Log.d("发送结果", "saveAndSendMessage: $succeed")
            }

            if (succeed) {
                callback.succeed(msg)
                msg.status = MessageStatus.SENT
            } else {
                callback.failed("发送失败")
                msg.status = MessageStatus.SENT_FAILED
            }
        }
    }

    override fun clearMessages(id: Int, type: MessageContentType) {
        // local work
    }


    override fun setMessageListener(listener: MessageListener) {
        this.listener = listener
    }

}