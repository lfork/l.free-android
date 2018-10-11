package com.lfork.a98620.lfree.imservice;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.base.FreeApplication;
import com.lfork.a98620.lfree.data.base.entity.IMUser;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.imservice.request.Request;
import com.lfork.a98620.lfree.imservice.request.Result;
import com.lfork.a98620.lfree.imservice.request.UserRequestType;
import com.lfork.a98620.lfree.util.JSONUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import static com.lfork.a98620.lfree.util.Print.print;


public class TCPConnection {

    private static final String TAG = "TCPConnection";

    private String URL;//连接信息：服务器地址

    private int PORT;//连接信息：端口

    private Socket socket = null; //连接Socket

    private PrintWriter out = null;

    private BufferedReader in = null;

    private IMUser u; //这个user 表示客户端的信息。 在第一次登录的时候会被设置

    private boolean connected = true, running = false;//连接状态

    public TCPConnection(String URL, int PORT) {
        this.URL = URL;
        this.PORT = PORT;
    }

    public void start() {
        try {
            buildConnection();
            keepAlive();
        } catch (IOException e) {
            print("TCPConnection->buildConnection()：服务端未开启，连接失败");
        }
    }

    private void buildConnection() throws IOException {
        socket = new Socket(URL, PORT);
        //控制乱码问题
        InputStreamReader inSR = new InputStreamReader(new DataInputStream(socket.getInputStream()), "UTF-8");
        in = new BufferedReader(inSR);
        //控制乱码问题
        OutputStreamWriter outSW = new OutputStreamWriter(new DataOutputStream(socket.getOutputStream()), "UTF-8");
        out = new PrintWriter(outSW);
        // status = true;
        print("TCPConnection.buildConnection()：服务器连接成功");
        setConnected(true);
    }

    /**
     * 重连后重新向服务器发送客户端的信息
     */
    private boolean rebindClientInfo() {
        Request<IMUser> request = new Request<IMUser>()
                .setData(u)
                .setRequestType(UserRequestType.DO_LOGIN)
                .setMessage("haha");

        if (!send(request.getRequest())) {
            print("网络连接中断");
            return false;
        }
        //这里还需要添加一个超时检测， 因为网络连接有问题的话 服务器是不会发送回执给客户端的
        //超时检测线程  -》 错了，应该在TCPConnection里面设置这个，

        String strResult = receive();

        Result<User> result = JSONUtil.parseJson(strResult, new TypeToken<Result<User>>() {
        });
        if (result != null) {
            switch (result.getCode()) {
                case 1:
                    print("连接成功");
                    return true;
                default:
                    Log.d(TAG, "rebindClientInfo: " + result);
                    print("连接失败");
                    return false;
            }
        }
        return false;
    }

    private void rebuildConnection() {
        try {
            print("正在重连....");
            buildConnection();
            //意思就是，在这里还需要发送客户端的连接信息给服务端，因为服务端需要连接信息来建立udp连接
            //连接成功后，还要将客户端的其他信息发送到服务器
            if (rebindClientInfo()) {
                print("重连成功");
            }
        } catch (IOException e) {
            // print("重连失败，继续重连");
            e.printStackTrace();
        }
    }

    /**
     * 采用心跳机制进行超时检测 。 服务端：开启超时检测线程。客户端如果超过时间后没有向服务端发送心跳检测消息，那么就说明客户端已经断线
     * 客户端：开启定时发送在线状态的线程。证明自己在线。 客户端还应该打开自检机制，比如自身无法连接互联网的时候，也要算作断线 断线后需要对用户进行提醒
     */
    private void keepAlive() {
        // 心跳机制证明自己在线
        FreeApplication.getDefaultThreadPool().execute(() -> {
            setRunning(true);
            System.out.println("超时检测已开启(心跳机制)");
            long aMinute = 60000;
            while (isRunning()) {
                if (isConnected()) {
                    try {
                        Thread.sleep(aMinute / 3);    //20秒发一次 ，弱网环境下的保持连接
                        // 这里对内容的封装 和服务端对内容的解析需要一一对应
                        // 这里就需要封装一个任务 发给服务器
                        send("keepAlive");
                        String feedBack = receiveOfKeepAlive();
                        if (feedBack != null) {
                            if (feedBack.equals("true")) {
//                            System.out.println("检测结果：连接正常");
                            }
                        }
                        //  System.out.println("服务器连接失败，正在重连");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("TcpConnection.keepAlive(). 未知异常 ");
                    }
                } else {  //这里就需要无限重连了，除非用户把程序给关了。
                    //停止keepAlive的工作，等待连接
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rebuildConnection();
                }

            }
        });
    }


    public boolean closeConnection() {
        try {
            setRunning(false);
            if (socket != null) {
                socket.close();
            }
            print("连接已关闭");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //保证同一个线程能顺利的完成接收和发送的操作
    public synchronized boolean send(String request) {
        if (isConnected()) {
//            print("TCPConnection->send 发送内容：" + request);
            if (out == null) {
                return false;
            }
            out.println(request);
            out.flush();
            return true;
        } else {
            return false;
        }

    }

    private String receiveOfKeepAlive() {
        try {

            synchronized (in) {
                int waitTimes = 2;
                while (!in.ready() && waitTimes >= 0) {      //普通操作应该在5秒内完成。 保连操作应该在2秒内完成
                    Thread.sleep(600);  //适当休眠 节约资源
                    waitTimes--;
                }
                if (waitTimes < 0) {
                    setConnected(false);
                    return null;
                }
                StringBuilder result = new StringBuilder();
                while (in.ready()) {
                    result.append(in.readLine());
                }
                return result.toString();
            }
//            print("TCPConnection.receiveOfKeepAlive 服务器的反馈结果:" + result);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String receive() {

        synchronized (in) {
            try {
                int waitTimes = 7;
                while (!in.ready() && waitTimes >= 0) {      //普通操作应该在5秒内完成。 保连操作应该在2秒内完成
                    Thread.sleep(600);  //适当休眠 节约资源
                    waitTimes--;
                }
                if (waitTimes < 0) {
                    setConnected(false);
                    return null;
                }
                StringBuilder result = new StringBuilder();
                int readtimes = 1;
                while (in.ready() && readtimes > 0) {       //只是一个缓解之策
                    result.append(in.readLine());       //在这里阻塞了？？？？？
                    readtimes--;
                }
                print("TCPConnection.receive 服务器的反馈结果:" + result);
                return result.toString();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private boolean isConnected() {
        return connected;
    }

    private void setConnected(boolean connected) {
        this.connected = connected;
        Config.setConnected(connected);
    }

    public boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public void setUser(IMUser u) {
        this.u = u;
    }
}
