package com.lfork.a98620.lfree.userinfo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.databinding.UserInfoActBinding;
import com.lfork.a98620.lfree.util.ToastUtil;

public class UserInfoActivity extends AppCompatActivity implements ViewModelNavigator{
    private int userId;
    private String username="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", 0);


        UserInfoActBinding binding = DataBindingUtil.setContentView(this, R.layout.user_info_act);
        UserInfoViewModel viewModel = new UserInfoViewModel(this, userId);

        viewModel.setNavigator(this);
        binding.setViewModel(viewModel);

        initActionBar("卖家信息");
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

                UserDataRepository userDataRepository = UserDataRepository.getInstance();
                User u = userDataRepository.getThisUser();
                if (u != null) {
                    if (u.getUserId() == userId){
                        ToastUtil.showShort(this, "不能和自己聊天");
                        break;
                    }
                    Intent intent = new Intent(this, ChatWindowActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("user_id", userId);
                    this.startActivity(intent);
                } else {
                    ToastUtil.showShort(this, "IM模块正在初始化...");
                }
                break;
                
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_action_bar, menu);
        MenuItem item = menu.getItem(0);
        item.setTitle("私聊");
        return true;
    }

    @Override
    public void notifyDataChanged() {

    }

    @Override
    public void setParam1(String param) {
        if (param == null) {
            username = userId +"";
        } else {
            username = param;
        }
    }

    @Override
    public void setParam2(String param) {

    }
}
