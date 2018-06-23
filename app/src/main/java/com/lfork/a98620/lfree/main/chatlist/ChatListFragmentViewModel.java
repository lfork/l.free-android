package com.lfork.a98620.lfree.main.chatlist;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.imdata.IMDataRepository;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 98620 on 2018/3/31.
 */

public class ChatListFragmentViewModel extends BaseViewModel {

    public final ObservableArrayList<ChatListItemViewModel> items = new ObservableArrayList<>();

    public final ObservableBoolean dataIsLoading = new ObservableBoolean(true);

    public final ObservableBoolean dataIsEmpty = new ObservableBoolean(true);

    private static final String TAG = "IndexFragmentViewModel";

    private FragmentActivity context;

    private IMDataRepository repository;

    private ChatListFragNavigator navigator;

    ChatListFragmentViewModel(FragmentActivity context) {
        this.context = context;
        new Thread(() -> repository = IMDataRepository.getInstance()).start();

    }

    private void loadUsers() {
        new Thread(() -> {
            repository.getChatUserList(new DataSource.GeneralCallback<List<User>>() {
                @Override
                public void succeed(List<User> data) {
                    //这里要在ui线程里面执行数据的更新操作
                    ArrayList<ChatListItemViewModel> items = new ArrayList<>();
                    for (User u : data) {
                        ChatListItemViewModel item = new ChatListItemViewModel(context, u);
                        items.add(item);
                        dataIsEmpty.set(false);
                    }
                    dataIsLoading.set(false);
                    notifyDataChanged(items);
                }
                @Override
                public void failed(String log) {
                    context.runOnUiThread(() -> {
                        Toast.makeText(context, log, Toast.LENGTH_LONG).show();
                    });
                }
            });
        }).start();
    }

    @Override
    public void start() {
        if (repository != null) {
            loadUsers();
        } else {
            ToastUtil.showShort(context, "可能没有网络...");
        }
    }

    @Override
    public void onDestroy() {

    }

    public void setNavigator(ChatListFragNavigator navigator) {
        this.navigator = navigator;
    }


    public void unbindNavigator() {
       this.navigator = null;
    }

    private void notifyDataChanged(ArrayList<ChatListItemViewModel> viewModels ){
        context.runOnUiThread(() -> {
            items.clear();
            items.addAll(viewModels);
            if (navigator != null) {
                navigator.notifyUsersChanged();
            }

        });

    }
}
