package com.lfork.a98620.lfree.chatwindow;

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
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.message.Message;
import com.lfork.a98620.lfree.data.source.IMDataRepository;
import com.lfork.a98620.lfree.data.source.UserDataRepository;
import com.lfork.a98620.lfree.data.source.remote.imservice.MessageListener;
import com.lfork.a98620.lfree.data.source.remote.imservice.MessageService;
import com.lfork.a98620.lfree.databinding.ChatWindowActBinding;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;
import com.lfork.a98620.lfree.util.ToastUtil;

public class ChatWindowActivity extends AppCompatActivity implements ChatWindowNavigator, MessageListener {

    //    private EditText editText;
    private RecyclerView recyclerView;

    private static final String TAG = "ChatWindowActivity";

//    private String username;

    private int userId;

    private int thisUserId;

    private MessageService.MessageBinder messageBinder;

    private ChatWindowActBinding binding;

    private ChatWindowViewModel viewModel;

    @Override
    protected void onResume() {
        super.onResume();
        if (messageBinder != null) {
            messageBinder.setListener(viewModel);
            viewModel.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.chat_window_act);
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);
        String username = intent.getStringExtra("username");

        viewModel = new ChatWindowViewModel(this);
        binding.setViewModel(viewModel);
        new Thread(() -> {
            messageBinder = IMDataRepository.getInstance().getBinder();
            if (messageBinder != null) {
                messageBinder.setListener(viewModel);
            } else {
                runOnUiThread(() -> {
                    ToastUtil.showShort(getApplicationContext(), "IM服务器异常,聊天功能开启失败");
                    finish();

                });
            }

        }).start();
        initActionBar(username);
        thisUserId = UserDataRepository.getInstance().getUserId();
        viewModel.setUserInfo(username, userId, thisUserId);
        viewModel.setNavigator(this);
//        initMeg(); //初始化几条message数据
        initUI();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (messageBinder != null)
            messageBinder.cancelListener();
    }

    private void initUI() {
        //消息界面的List设置
        recyclerView = (RecyclerView) findViewById(R.id.message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MessageListAdapter adapter = new MessageListAdapter(viewModel.messages);
        recyclerView.setAdapter(adapter);

//        Button send = (Button) findViewById(R.id.btn_send);
//        editText = (EditText) findViewById(R.id.edit_window);
//        send.setOnClickListener(view -> {
//            String message_sent = editText.getText().toString();
//            if (!message_sent.equals("")) {
//
//                Message m;
//
////                    if ((int)(Math.random() * 10) % 2 == 0) {
//                m = new Message(message_sent, Message.SendType);
//
//                m.setSenderID(thisUserId);
//                m.setReceiverID(userId);
//                m.setType(MessageType.NORMAL_MESSAGE);
//                m.setContentType(MessageContentType.COMMUNICATION_USER);
//                m.setMessageID(System.currentTimeMillis());
////                    } else {
////                        msg = new Message(message_sent, Message.ReceiveType);
////                    }
//                messageList.add(m);
//                new Thread(() -> {
//                    sendMessage(m, new DataSource.GeneralCallback<Message>() {
//                        @Override
//                        public void succeed(Message data) {
//                            runOnUiThread(() -> ToastUtil.showShort(getApplicationContext(), "发送成功"));
//                        }
//
//                        @Override
//                        public void failed(String log) {
//                            runOnUiThread(() -> ToastUtil.showShort(getApplicationContext(), log));
//                        }
//                    });
//                }).start();
//
//                adapter.notifyItemInserted(messageList.size() - 1); //当有新消息时，刷新RecyclerView
//                recyclerView.scrollToPosition(messageList.size() - 1);//将recyclerView定位到最后一行
//                editText.setText("");//清空输入框的内容
//            }
//
//        });
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
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                intent.putExtra("user_id", userId);
                startActivityForResult(intent, 4);
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

    public void sendMessage(Message message, DataSource.GeneralCallback<Message> callback) {
        recyclerView.scrollToPosition(viewModel.messages.size() - 1);//将recyclerView定位到最后一行
        new Thread(() -> {
            if (messageBinder != null) {
                messageBinder.sendMessage(message, callback);
            }
        }).start();
//        adapter.notifyItemInserted(messageList.size() - 1); //当有新消息时，刷新RecyclerView
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

    @Override
    public void onReceived(Message message) {


    }
}
