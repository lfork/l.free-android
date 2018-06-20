package com.lfork.a98620.lfree.main.chatlist;

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
import com.lfork.a98620.lfree.databinding.MainChatListFragBinding;
import com.lfork.a98620.lfree.util.adapter.ListViewAdapter;

public class ChatListFragment extends Fragment implements ChatListFragNavigator {

    private static final String TAG = "ChatListFragment";

    private MainChatListFragBinding binding;
    private ChatListFragmentViewModel viewModel;
    private View rootView;// 缓存Fragment view@Override
    private ListView listView;
    private ListViewAdapter<ChatListItemViewModel> adapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        //虽然是执行了onDestroy 但是models 依然是models ，里面的数据是没有被释放的。 因为mainactivity还持有fragment的引用。
        //也就是说fragment destory后 依然能够重新启动
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.setNavigator(this);
        viewModel.start();

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
        listView = binding.mainChatList;
        adapter = new ListViewAdapter<>(getContext(), R.layout.main_chat_list_contacts_item, viewModel.items, BR.viewModel); //BR 里面的entity 是经过处理的entity
        listView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (viewModel != null) {
            viewModel.unbindNavigator();
        }
    }

    @Override
    public void notifyUsersChanged() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void openChatWindow(int userId, String userName) {

    }

//

}
