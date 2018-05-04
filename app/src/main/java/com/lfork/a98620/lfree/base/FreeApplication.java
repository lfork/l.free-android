package com.lfork.a98620.lfree.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by 98620 on 2018/3/19.
 */

public class FreeApplication extends Application{
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
