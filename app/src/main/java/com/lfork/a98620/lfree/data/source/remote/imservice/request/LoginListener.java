package com.lfork.a98620.lfree.data.source.remote.imservice.request;

import com.lfork.a98620.lfree.data.entity.User;

public interface LoginListener {
    void succeed(User user);
//    void passwordError();
//
//    void noSuchUser();
//
//    void connectError();
    void failed(String log);

}
