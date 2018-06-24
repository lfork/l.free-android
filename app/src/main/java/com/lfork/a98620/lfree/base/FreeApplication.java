package com.lfork.a98620.lfree.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by 98620 on 2018/3/19.
 */

public class FreeApplication extends Application{
    /**
     * 这里因为是application context 所以就没有内存泄漏的问题，
     * 因为application的生命周期是最长的
     * application是最后被jvm释放掉的
     */
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initDataBase();
    }

    private void initDataBase(){
        LitePal.initialize(context);
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        FreeApplication.context = context;
    }
}
