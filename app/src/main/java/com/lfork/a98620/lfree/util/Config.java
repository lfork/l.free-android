package com.lfork.a98620.lfree.util;

/**
 * Created by 98620 on 2018/4/7.
 */

public class Config {
    public static String ServerURL = "http://www.lfork.top";

    public static String ServerImagePathRoot = "http://www.lfork.top/image/";

    public static String ServerURLTest = "http://www.lfork.top";

    private static boolean connected;  //这个同tcp 的 connected保持一致

    public static boolean isConnected() {
        return connected;
    }

    public static void setConnected(boolean connected) {
        Config.connected = connected;
    }

    //头像接口， 
}
