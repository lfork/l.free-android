package com.lfork.a98620.lfree.main.chatlist;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.lfork.a98620.lfree.common.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.IMDataRepository;

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
//        binding.searchBtn.clearFocus(); //取消searchView的焦点
        repository = IMDataRepository.getInstance();
    }


    private void loadUsers() {
        new Thread(() -> {
            repository.getChatUserList(new DataSource.GeneralCallback<List<User>>() {
                @Override
                public void succeed(List<User> data) {
                    items.clear();
                    for (User u : data) {
                        ChatListItemViewModel item = new ChatListItemViewModel(context, u);
                        items.add(item);

                        dataIsEmpty.set(false);
                    }
                    dataIsLoading.set(false);
                    navigator.notifyMessagesChanged();

//                    context.runOnUiThread(() -> {
////                        ToastUtil.showShort(context, "联系人列表加载成功");
//
//                    });
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

    public void start() {
        loadUsers();
    }

    public void setNavigator(ChatListFragNavigator navigator) {
        this.navigator = navigator;
    }

    //    private void initIMList() {
//        List<IM> contactsList = new ArrayList<>();
//        for (int i = 0; i < 15; i++) {
//            IM goods = new IM();
//            goods.setName("goods" + i);
//            goods.setId(i);
//            goods.setPrice(String.valueOf(i * 100));
//            goods.setCoverImagePath("???");
//            contactsList.add(goods);
//            Log.d(TAG, "initIMList: test1" + i);
//        }
//
//        for (IM g : contactsList) {
//            models.add(new ChatListItemViewModel(getContext()));
//
//        }
//        Log.d(TAG, "initIMList: " + models.size());
//    }
}
