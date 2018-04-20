package com.lfork.a98620.lfree.data.entity.message;

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

    public static Message messageParse(DatagramPacket dgp){
        Message message = null;
        try {
           String str =  new String(dgp.getData(), 0, dgp.getLength(), "utf-8");
           message = JSONUtil.parseJson(str, new TypeToken<Message>(){});
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //不对这里还需要将 字符串的message解析为Message对象才行
        return message;
    }
}
