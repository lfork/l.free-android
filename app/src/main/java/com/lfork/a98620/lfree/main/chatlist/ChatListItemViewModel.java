package com.lfork.a98620.lfree.main.chatlist;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.util.Log;

import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.common.BaseViewModel;
import com.lfork.a98620.lfree.data.entity.User;

/**
 * Created by 98620 on 2018/4/9.
 */

public class ChatListItemViewModel extends BaseViewModel {
    private static final String TAG = "ChatListItemViewModel";

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> newMessage = new ObservableField<>();

    public final ObservableField<String> newMessageTime = new ObservableField<>("没有消息");

    public final ObservableField<String> imageUrl = new ObservableField<>();

    private int userId;

    ChatListItemViewModel(Context context, User user) {
        super(context);
        username.set(user.getUserName());
        imageUrl.set(user.getUserImagePath());
        userId = user.getUserId();
    }

    public void onClick(){
        Log.d(TAG, "onButton1Clicked: ???" );
        Intent intent = new Intent(context, ChatWindowActivity.class);
        intent.putExtra("user_id", userId);
        context.startActivity(intent);
    }

}
