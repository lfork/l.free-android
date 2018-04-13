package com.lfork.a98620.lfree.chatwindow;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;
import com.lfork.a98620.lfree.userinfothis.UserInfoThisActivity;
import com.lfork.a98620.lfree.userinfothis.UserInfoThisEditActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatWindowActivity extends AppCompatActivity {

    private List<Message> messageList = new ArrayList<>();
    private EditText editText;
    private  RecyclerView recyclerView;
    private MessageListAdapter adapter;

    private static final String TAG = "ChatWindowActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_window_act);
        Intent intent = getIntent();
        String userName = intent.getIntExtra("seller_id", -1) + "";
        initActionBar(userName);
        initMeg(); //初始化几条message数据
        initUI();

    }

    private void initUI(){
        //消息界面的List设置
        recyclerView = (RecyclerView) findViewById(R.id.message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MessageListAdapter(messageList);//适配器，处理，获取内容
        recyclerView.setAdapter(adapter);

        Button send = (Button) findViewById(R.id.btn_send);
        editText = (EditText) findViewById(R.id.edit_window);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message_sent = editText.getText().toString();
                if (!message_sent.equals("")){
                    Message msg;
                    if ((int)(Math.random() * 10) % 2 == 0) {
                        msg = new Message(message_sent, Message.SendType);
                    } else {
                        msg = new Message(message_sent, Message.ReceiveType);
                    }
                    messageList.add(msg);
                    adapter.notifyItemInserted(messageList.size() - 1); //当有新消息时，刷新RecyclerView
                    recyclerView.scrollToPosition(messageList.size() - 1);//将recyclerView定位到最后一行
                    editText.setText("");//清空输入框的内容
                }

            }
        });
    }

    //初始化几条消息

    //发送按钮的监听     1、刷新recyclerView   2、editText的清空  我点一下按钮 然后我就需要重新绘制一下界面

    //加载几条信息到List上面去


    private void initMeg() {
        Log.d(TAG, "onClick: 测试1");
        Message apple = new Message("Hey~, guy", Message.SendType);
        messageList.add(apple);
        Message banana = new Message("Hello, who is that?", Message.ReceiveType);
        messageList.add(banana);
        Message orange = new Message("I am King.", Message.SendType);
        messageList.add(orange);
        Message watermelon = new Message("Ok ok , nice to call you", Message.ReceiveType);
        messageList.add(watermelon);

    }

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
                Intent intent = new Intent(ChatWindowActivity.this, UserInfoActivity.class);
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

}
