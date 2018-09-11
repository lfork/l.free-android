package com.lfork.a98620.lfree.main.chatlist;

import android.content.Context;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.base.Config;

import java.lang.ref.WeakReference;

/**
 * Created by 98620 on 2018/4/9.
 */

public class ChatListItemViewModel extends BaseViewModel {

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> newMessage = new ObservableField<>();

    public final ObservableField<String> newMessageTime = new ObservableField<>("没有消息");

    public final ObservableField<String> imageUrl = new ObservableField<>();

    private int userId;

    public WeakReference<ChatListItemNavigator> reference;

    ChatListItemViewModel(Context context, User user) {
        super(context);

        username.set(user.getUserName());
        imageUrl.set(Config.ServerURL + "/image" + user.getUserImagePath());
        userId = user.getUserId();
    }

    public void onClick() {
        if (reference != null && reference.get() != null) {
            reference.get().openChatWindow(userId, username.get());
        }
    }


    @Override
    public void setNavigator(Object navigator) {
        super.setNavigator(navigator);
        reference = new WeakReference<>((ChatListItemNavigator) navigator);
    }

}
