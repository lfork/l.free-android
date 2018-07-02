package com.lfork.a98620.lfree.main.chatlist;

import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;

/**
 * Created by 98620 on 2018/4/22.
 */
public interface ChatListItemNavigator extends com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator {

    void openChatWindow(int userId, String userName);
}
