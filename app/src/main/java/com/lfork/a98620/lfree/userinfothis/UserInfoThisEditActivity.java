package com.lfork.a98620.lfree.userinfothis;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.UserInfoThisEditActBinding;

public class UserInfoThisEditActivity extends AppCompatActivity {

    UserInfoThisViewModel viewModel ;

    UserInfoThisEditActBinding binding;

    //刷新一下数据
    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null){
            viewModel.refreshData();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.user_info_this_edit_act);
        viewModel = new UserInfoThisViewModel(this);
        binding.setViewModel(viewModel);
        initActionBar();
    }

    public void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("用户信息修改");
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
                viewModel.updateUserInfo();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_action_bar, menu);
        MenuItem item = menu.getItem(0);
        item.setTitle("完成");
        return true;
    }
}
