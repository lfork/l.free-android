package com.lfork.a98620.lfree.chatwindow;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.FreeApplication;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.imdata.IMDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.databinding.ChatWindowActBinding;
import com.lfork.a98620.lfree.imservice.MessageService;
import com.lfork.a98620.lfree.imservice.message.Message;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;
import com.lfork.a98620.lfree.util.ToastUtil;

public class ChatWindowActivity extends AppCompatActivity implements ChatWindowNavigator{

    private RecyclerView recyclerView;

    private int userId;

    private MessageService.MessageBinder messageBinder;

    private ChatWindowViewModel viewModel;

    @Override
    protected void onResume() {
        super.onResume();
        if (messageBinder != null) {
            messageBinder.setListener(viewModel.listener, userId);
            viewModel.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatWindowActBinding binding = DataBindingUtil.setContentView(this, R.layout.chat_window_act);
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);
        String username = intent.getStringExtra("username");

        viewModel = new ChatWindowViewModel(this);
        binding.setViewModel(viewModel);
        FreeApplication.executeThreadInDefaultThreadPool(() -> {
            messageBinder = IMDataRepository.Companion.getInstance().getBinder();
            if (messageBinder != null) {
                messageBinder.setListener(viewModel.listener, userId);
            } else {
                runOnUiThread(() -> {
                    ToastUtil.showShort(getApplicationContext(), "IM服务器异常,聊天功能开启失败");
                    finish();

                });
            }

        });
        initActionBar(username);
        int thisUserId = UserDataRepository.INSTANCE.getUserId();
        viewModel.setUserInfo(username, userId, thisUserId);
        viewModel.setNavigator(this);
        initUI();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (messageBinder != null) {
            messageBinder.cancelListener();
        }
    }

    private void initUI() {
        //消息界面的List设置
        recyclerView = (RecyclerView) findViewById(R.id.message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MessageListAdapter adapter = new MessageListAdapter(viewModel.messages);
        recyclerView.setAdapter(adapter);
    }

    //初始化几条消息

    //发送按钮的监听     1、刷新recyclerView   2、editText的清空  我点一下按钮 然后我就需要重新绘制一下界面

    //加载几条信息到List上面去


    public void initActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu1:
                UserInfoActivity.activityStart(ChatWindowActivity.this, userId);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_action_bar, menu);
        MenuItem item = menu.getItem(0);
        item.setTitle("卖家信息");
        return true;
    }

    @Override
    public void sendMessage(Message message, DataSource.GeneralCallback<Message> callback) {
        recyclerView.scrollToPosition(viewModel.messages.size() - 1);//将recyclerView定位到最后一行
        if (messageBinder != null) {
            messageBinder.sendMessage(message, callback);
        }
    }


    @Override
    public void showToast(String content) {
        runOnUiThread(() -> ToastUtil.showShort(getApplicationContext(), content));
    }

    @Override
    public void notifyMessagesChanged() {
        runOnUiThread(() -> {
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scrollToPosition(viewModel.messages.size() - 1);//将recyclerView定位到最后一行
        });
    }


    public static void activityStart(Context context, String username, int userId) {
        Intent intent = new Intent(context, ChatWindowActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("user_id", userId);
        context.startActivity(intent);
    }

}
