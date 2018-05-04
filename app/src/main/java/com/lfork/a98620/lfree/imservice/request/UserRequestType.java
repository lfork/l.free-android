package com.lfork.a98620.lfree.imservice.request;

/**
 * 这个文件在客户端和服务器需要保持一致
 */
public enum UserRequestType {

    DO_LOGIN(0),

    DO_REGISTER(1),

    DO_LOGOUT(15),

    GET_USER_INFO(2),

    UPDATE_USER_INFO(3),

    ADD_FRIEND(4),

    DELETE_FRIEND(5),

    GET_FRIEND_INFO(6),

    UPDATE_FRIEND_INFO(7),

    ADD_GROUP(8),

    DELETE_GROUP(9),

    UPDATE_GROUP(10),

    STRAT_MESSAGE_LISTENER(11),

    GET_FRIEND_LIST(12),

    GET_USERS(13),

    ALIVE_TEST(14);

    private int value;

    UserRequestType(int i) {
        this.value = i;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}