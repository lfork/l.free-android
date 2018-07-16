package com.lfork.a98620.lfree.imservice;

import com.lfork.a98620.lfree.base.FreeApplication;
import com.lfork.a98620.lfree.imservice.message.Message;
import com.lfork.a98620.lfree.imservice.message.MessageHelper;
import com.lfork.a98620.lfree.imservice.message.MessageType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lfork.a98620.lfree.util.Print.print;

/**
 * 负责的功能：
 * 提供UDP消息的发送接口
 * 负责接收来自服务器的消息并储存
 * UDP保连功能(需要再开一个线程)
 */
public class UDPConnection extends Thread {

    private DatagramSocket socket = null;

    //TODO 这里的buffer 可能会有一些问题
    private byte[] buffer = null;

    private InetAddress address = null;

    private String URL;

    private int serverPort;

    private int userId;

    private boolean running = false;

    private List<Message> messageReceiveQueue;   //待处理的消息

    private Map<String, String> feedBackRepository = new HashMap<>();  //为每条普通消息建立反馈消息仓库， 如果接收到了反馈信息，那么就立马将这条反馈信息储存到这里。


    public UDPConnection(int userId, String URL, int serverPort, List<Message> messageReceiveQueue) {
        this.messageReceiveQueue = messageReceiveQueue;
        this.URL = URL;
        this.serverPort = serverPort;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            buildConnection();
            keepAlive();
            startReceiveListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReceiveListener() {
        while (isRunning()) {
//            Log.d("消息接收监听", "startReceiveListener: 接收不到消息吗？");
            Message message = MessageHelper.messageParse(receive());
            if (message != null) {
                switch (message.getType()) {
                    case CONNECTION_INFO:
                        break;
                    case NORMAL_MESSAGE:
                        messageReceiveQueue.add(message);
                        sendFeedBack(message);
                        break;
                    case FEEDBACK:
                        feedBackRepository.put(message.getMessageID() + "", "ok");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void buildConnection() throws IOException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(URL);
        buffer = new byte[65527];
        setRunning(true);

    }

    private void keepAlive() {
        FreeApplication.getDefaultThreadPool().execute(() -> {
            while (isRunning()) {
                if (Config.isConnected()) {
                    Message msg = new Message();
                    msg.setType(MessageType.CONNECTION_INFO);
                    msg.setContent(userId + "");
                    msg.setSenderID(userId);
                    byte[] dataToServer = (msg.toString()).getBytes();

                    DatagramPacket out = new DatagramPacket(dataToServer, dataToServer.length, address, serverPort);  //serverPort
                    try {
                        socket.send(out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 300秒就保活一次
                    // print("UDPConnection.keepAlive():保活消息已发送(30秒一次)");
                    try {
                        Thread.sleep(1000 * 30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    //缩短休眠时间，以便重连之后能马上激活服务端的udp服务
//                    System.out.println("UDPConnection.keepAlive()://缩短休眠时间，以便重连之后能马上激活服务端的udp服务");
                    // print("UDPConnection.keepAlive():保活消息未发送  当前连接无效");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //难道是保连消息没有收到？？？
            }
        });
    }

    public void rebuildConnection() {
        Message msg = new Message();
        msg.setType(MessageType.CONNECTION_INFO);
        msg.setContent(userId + "");
        msg.setSenderID(userId);
        byte[] dataToServer = (msg.toString()).getBytes();

        DatagramPacket out = new DatagramPacket(dataToServer, dataToServer.length, address, serverPort);  //serverPort
        try {
            socket.send(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 这里还应该加上 消息回执的处理
     *
     * @param message 需要发送的消息
     */
    private void send(String message) {  //同步的问题？？

        try {
            byte[] msg = message.getBytes("utf-8");
            DatagramPacket out = new DatagramPacket(msg, msg.length, address, serverPort);
            socket.send(out);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param message msg
     * @return e
     */
    public boolean sendNormalMessage(Message message) {
        String content = message.toString();
        //Log.d("发送的内容", "sendNormalMessage: " + content);
        send(content);
        return getFeedBack(message.getMessageID() + "");
    }

    private void sendFeedBack(Message message) {
        Message feedBack = new Message();
        feedBack.setReceiverID(message.getSenderID());
        feedBack.setType(MessageType.FEEDBACK);
        //feedBack.setContent("Message接收成功,MessageID:" + message.getMessageID());
        feedBack.setMessageID(message.getMessageID());
        send(feedBack.toString());
    }

    /**
     * 超时时间为1秒
     *
     * @param messageId 消息的id
     * @return 有反馈返回true
     */
    private boolean getFeedBack(String messageId) {
        boolean sendSucceed = false;
        int i = 10;
        while (i > 0) {
            i--;
            try {
                sleep(100);  //适当休眠，设置等待时间：1秒
                sendSucceed = feedBackRepository.get(messageId) != null;
                if (sendSucceed) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sendSucceed;
    }

    private DatagramPacket receive() {
        // receive会自动的卡在这里 ， 看来在这里还需要开一个子线程来接收消息
        byte[] buffer = new byte[65000];
        DatagramPacket in = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    /**
     * 关闭UDP 连接
     */
    public void closeConnection() {
        if (socket != null) {
            socket.close();
        }
        print("UDP 连接已关闭");
        setRunning(false);
    }

    private boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }
}
