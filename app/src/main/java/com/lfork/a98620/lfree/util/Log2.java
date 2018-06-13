package com.lfork.a98620.lfree.util;

import android.util.Log;

/**
 * Created by 98620 on 2018/6/13.
 */
public class Log2 {
    private static final String TAG = "Log2";

    public static void d(String data){
        Log.d(Thread.currentThread().getName(), "succeed: " + data);
    }
}
