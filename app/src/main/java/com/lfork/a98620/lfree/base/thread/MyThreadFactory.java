package com.lfork.a98620.lfree.base.thread;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

/**
 * Created by 98620 on 2018/7/16.
 */
public class MyThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(@NonNull Runnable r) {

        Thread t = new Thread(r);

        t.setName("ThreadPool");

        return null;
    }
}
