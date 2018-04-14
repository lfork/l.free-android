package com.lfork.a98620.lfree.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 98620 on 2018/4/14.
 */

public class ToastUtil {
    public static void showLong(Context context, String content){
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
