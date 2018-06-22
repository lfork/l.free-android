package com.lfork.a98620.lfree.imservice.message;

import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.util.JSONUtil;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;

public class MessageHelper {
//    public static UDPConnectionInfo UDPConnectionInfoParse(DatagramPacket dgp) {
//        InetAddress address = dgp.getAddress();
//        int port = dgp.getPort();
//        return new UDPConnectionInfo(address, port);
//    }

    public static Message messageParse(DatagramPacket dgp) {
//        Message message = null;
//
//        byte[] bs = new byte[1000];
//
//        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(dgp.getData(), 0, dgp.getLength()));
//
//        String msg = null;
//        try {
//            while (dis.available() > 0) {
//                // reads characters encoded with modified UTF-8
//                int index = dis.read(bs);
//                msg = Arrays.toString(bs);
//                // print
//               print( msg + " ");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        //不对这里还需要将 字符串的message解析为Message对象才行
//        //           String str =  new String(dgp.getData(), 0, dgp.getLength(), "utf-8");
//        print("UDPConnection.startReceiveListener(): 接收到的信息：" + msg);
//        message = JSONUtil.parseJson(msg, new TypeToken<Message>() {
//        });
//
//        //不对这里还需要将 字符串的message解析为Message对象才行
//        return message;
////
        Message message = null;
        try {
            String str =  new String(dgp.getData(), 0, dgp.getLength(), "utf-8");
//            print("UDPConnection.startReceiveListener(): 接收到的信息：" + str );
            message = JSONUtil.parseJson(str, new TypeToken<Message>(){});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //不对这里还需要将 字符串的message解析为Message对象才行
        return message;
    }
}
