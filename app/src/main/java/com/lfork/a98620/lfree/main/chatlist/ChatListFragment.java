package com.lfork.a98620.lfree.main.chatlist;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.databinding.MainChatListFragBinding;
import com.lfork.a98620.lfree.base.adapter.ListViewAdapter;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.Objects;

public class ChatListFragment extends Fragment implements ChatListItemNavigator {

    private MainChatListFragBinding binding;

    private ChatListFragmentViewModel viewModel;

    private View rootView;// 缓存Fragment view

    private ListViewAdapter adapter;


    @Override
    public void onResume() {
        super.onResume();
        viewModel.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
        adapter.onDestroy();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.main_chat_list_frag, container, false);
            viewModel = new ChatListFragmentViewModel(getActivity());
            binding.setViewModel(viewModel);
            setupListView();
            rootView = binding.getRoot();

        } else {
            // 缓存的rootView需要判断是否已经被加过parent，
            // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void setupListView() {
        ListView listView = binding.mainChatList;
        adapter = new ListViewAdapter<ChatListItemViewModel>(getContext(), R.layout.main_chat_list_contacts_item, BR.viewModel);
        adapter.setNavigator(this);
        listView.setAdapter(adapter);
    }


    @Override
    public void openChatWindow(int userId, String userName) {
        Intent intent = new Intent(getContext(), ChatWindowActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("username", userName);
        startActivity(intent);
    }

    @Override
    public void showToast(String msg) {
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> ToastUtil.showShort(getContext(), msg));
    }

}
