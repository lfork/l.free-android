package com.lfork.a98620.lfree.userinfo;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.BaseActivity;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.databinding.UserInfoActBinding;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.Objects;

public class UserInfoActivity extends BaseActivity implements ViewModelNavigator {

    private UserInfoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int userId = intent.getIntExtra("user_id", 0);

        UserInfoActBinding binding = DataBindingUtil.setContentView(this, R.layout.user_info_act);

        viewModel = new UserInfoViewModel(this, userId);
        viewModel.setNavigator(this);
        binding.setViewModel(viewModel);
        initActionBar("卖家信息");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.setNavigator(this);
            viewModel.start();
        }
    }

    public void initActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(true);
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
                UserDataRepository repository = UserDataRepository.INSTANCE;
                if (repository.getUserId() == viewModel.getUserId()) {
                    ToastUtil.showShort(this, "不能和自己聊天");
                    break;
                }
                Intent intent = new Intent(this, ChatWindowActivity.class);
                intent.putExtra("username", viewModel.username.get());
                intent.putExtra("user_id", viewModel.getUserId());
                this.startActivity(intent);
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

    public static void activityStart(Context context, int userId) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("user_id", userId);
        context.startActivity(intent);
    }


    @Override
    public void showToast(String msg) {
        runOnUiThread(() -> {
            ToastUtil.showShort(getBaseContext(), msg);
        });
    }
}
