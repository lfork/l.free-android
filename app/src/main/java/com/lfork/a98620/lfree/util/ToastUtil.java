package com.lfork.a98620.lfree.util;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by 98620 on 2018/4/14.
 */

public class ToastUtil {
    public static void showLong(Context context, String content){
        if (!Thread.currentThread().getName().equals("main")) {
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }
        }
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String content){
        if (!Thread.currentThread().getName().equals("main")) {     //不能在非主线程里面直接Toast   Can't create handler inside thread that has not called Looper.prepare()
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }
        }
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
