package com.lfork.a98620.lfree.imservice;

public class Config {
//    public static String URL = "192.168.1.228";
//    public static String URL = "192.168.1.63";
    public static String URL = "www.lfork.top";  //

    private static boolean connected;  //这个同tcp 的 connected保持一致

    public static boolean isConnected() {
        return connected;
    }

    public static void setConnected(boolean connected) {
        Config.connected = connected;
    }
}