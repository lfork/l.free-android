package com.lfork.a98620.lfree.util;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * Created by 98620 on 2018/6/13.
 */
public class TimeUtil {
    public static String getStandardTime(){
         return DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString();
    }

    public static String getBiggestTime(){
        return DateFormat.format("9999-01-02 13:44:44", new Date()).toString();
    }
}
