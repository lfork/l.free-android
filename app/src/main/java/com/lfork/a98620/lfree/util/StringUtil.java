package com.lfork.a98620.lfree.util;

/**
 * Created by 98620 on 2018/4/8.
 */

public class StringUtil {
    public static boolean isNull(String str) {
        return str == null || str.equals("") || str.length() == 0;
    }
}
