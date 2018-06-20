package com.lfork.a98620.lfree.main.chatlist;

/**
 * Created by 98620 on 2018/4/22.
 */
public interface ChatListFragNavigator {
    void notifyUsersChanged();

    void openChatWindow(int userId, String userName);
}
