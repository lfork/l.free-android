package com.lfork.a98620.lfree.main.chatlist;

import android.content.Context;
import android.content.Intent;

import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.common.BaseViewModel;
import com.lfork.a98620.lfree.goodsdetail.GoodsDetailActivity;

/**
 * Created by 98620 on 2018/4/9.
 */

public class ChatListItemViewModel extends BaseViewModel {

    public ChatListItemViewModel(Context context) {
        super(context);
    }

    public void onClick(){
        Intent intent = new Intent(getContext(), ChatWindowActivity.class);
        getContext().startActivity(intent);
    }
}
