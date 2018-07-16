package com.lfork.a98620.lfree.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 98620 on 2018/3/19.
 */

public class FreeApplication extends Application implements MyApplication {

    public final static String APP_SHARED_PREF = "application_shared_pref";

    private static ExecutorService executorService;

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
        initThreadPool();
    }

    @Override
    public void initDataBase() {
        LitePal.initialize(context);
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        FreeApplication.context = context;
    }


    /**
     *
     */
    @Override
    public void initThreadPool() {
        executorService = new ThreadPoolExecutor(
                Config.BASE_THREAD_POOL_SIZE,
                Config.BASE_THREAD_POOL_SIZE * 2,
                0L,
                TimeUnit.MICROSECONDS,
                new LinkedBlockingDeque<>(),
                Executors.defaultThreadFactory());
    }

    public static ExecutorService getAppFixedThreadPool() {
        return executorService;
    }

    public static ExecutorService getDefaultThreadPool() {
        return executorService;
    }

    public static void executeThreadInDefaultThreadPool(Runnable r) {
        executorService.execute(r);
    }
}
