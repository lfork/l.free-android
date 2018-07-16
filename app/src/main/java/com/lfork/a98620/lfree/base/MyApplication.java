package com.lfork.a98620.lfree.base;

/**
 *
 * @author 98620
 * @date 2018/7/16
 */
public interface MyApplication {

    /**
     * 初始化当前应用(进程)的线程池
     */
    void initThreadPool();


    /**
     * 初始化数据库
     */
    void initDataBase();
}
