package com.lfork.a98620.lfree.data;

/**
 * Created by 98620 on 2018/3/19.
 */

public interface DataSource {
    interface GeneralCallback<T>{
        void success(T data);
        void failed(String log);
    }
}
