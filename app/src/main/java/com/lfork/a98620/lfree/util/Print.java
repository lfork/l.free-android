package com.lfork.a98620.lfree.util;

import android.util.Log;

public class Print {
    private static final String TAG = "Print";
    public static void print(String content){
        Log.d(TAG, "print: " + content);
    }

    public static void print(Object content){
        Log.d(TAG, "print: " + content);
    }

    public static void print(String Tag, String content){
        Log.d(Tag, "print: " + content);
    }
}
