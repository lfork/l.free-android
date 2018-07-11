package com.lfork.a98620.lfree.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by 98620 on 2018/6/20.
 */
public class ShareUtil {
    private static final String TAG = "ShareUtil";
    public static void shareTextBySystem(Context context, String text, String tips){
        if (context == null || TextUtils.isEmpty(text) || TextUtils.isEmpty(tips)) {
            Log.d(TAG, "shareTextBySystem: 参数不能为空");
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND)
                .setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent,  tips));
    }
}
