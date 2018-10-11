package com.lfork.a98620.lfree.main.chatlist;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.base.FreeApplication;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.data.imdata.IMDataRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 98620 on 2018/3/31.
 */

public class ChatListFragmentViewModel extends BaseViewModel {

    public final ObservableArrayList<ChatListItemViewModel> mItems = new ObservableArrayList<>();

    public final ObservableBoolean dataIsLoading = new ObservableBoolean(true);

    public final ObservableBoolean dataIsEmpty = new ObservableBoolean(true);

    private IMDataRepository repository;

    private ViewModelNavigator navigator;

    ChatListFragmentViewModel(Context context) {
        super(context);
    }

    private void getChatList() {
        FreeApplication.executeThreadInDefaultThreadPool(() -> {
            repository = IMDataRepository.Companion.getInstance();
            repository.getChatUserList(new DataSource.GeneralCallback<List<User>>() {
                @Override
                public void succeed(List<User> data) {
                    //这里要在ui线程里面执行数据的更新操作
                    ArrayList<ChatListItemViewModel> items = new ArrayList<>();
                    for (User u : data) {
                        ChatListItemViewModel item = new ChatListItemViewModel(getContext(), u);
                        items.add(item);
                        dataIsEmpty.set(false);
                    }
                    mItems.clear();
                    mItems.addAll(items);
                    dataIsLoading.set(false);

                }

                @Override
                public void failed(String log) {
                    showToast(log);
                }
            });
        });
    }

    @Override
    public void start() {
        getChatList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigator = null;
    }

    public void setNavigator(ChatListItemNavigator navigator) {
        super.setNavigator(navigator);
    }
}
