package com.lfork.a98620.lfree.imservice;

import android.util.Log;

import com.lfork.a98620.lfree.data.imdata.remote.MessageRemoteDataSource;
import com.lfork.a98620.lfree.imservice.message.Message;

import java.util.List;

/**
 * 需要将服务器发来的消息进行分发。 分发给Repository
 */
public class UDPMessageMaid extends Thread {

    private static final String TAG = "UDPMessageMaid";

    private boolean running;

    private MessageRemoteDataSource dataSource;

    private List<Message> messageReceiveQueue;   //待处理的消息

    public UDPMessageMaid(MessageRemoteDataSource dataSource, List<Message> messageReceiveQueue) {
        this.dataSource = dataSource;
        this.messageReceiveQueue = messageReceiveQueue;
    }

    @Override
    public void run() {
        Message message = null;
        setRunning(true);
        while (isRunning()) {
            Log.d(TAG, "run: UDPMaid运行中");
            while (message == null) {
                if (messageReceiveQueue.size() > 0 && dataSource.getListener() != null) {
                    message = messageReceiveQueue.remove(0);
                } else {
                    try {
                        sleep(100); //适当休眠，降低cpu负载
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //处理信息:储存 + 发送
            dealMessage(message);
            message = null;
        }

        Log.d(TAG, "run: UDPMaid任务结束");
    }

    /**
     * 把接收到的消息进行分类储存 ，信息记录(聊天记录).  信息记录 。然后再把这条消息发给接收者。
     * 先这样，先不管储存的事情，而是先把消息推送到前台
     *
     * @param message
     */
    private void dealMessage(Message message) {
        dataSource.pushMessage(message);
    }

    /**
     * 如果客户端下线了， 那么就需要释放客户端在服务器所对应的处理线程
     */
    public void close() {
        //这里需要将 clientID 和 messageQueue里面的内容进行保存
        setRunning(false);
    }


    public boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public void addMessage(Message message) {
        messageReceiveQueue.add(message);
    }

}
