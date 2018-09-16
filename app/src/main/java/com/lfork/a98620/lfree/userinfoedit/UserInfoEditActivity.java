package com.lfork.a98620.lfree.userinfoedit;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.base.entity.School;
import com.lfork.a98620.lfree.databinding.UserInfoThisEditActBinding;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.List;
import java.util.Objects;

public class UserInfoEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, UserInfoEditNavigator {

    UserInfoEditViewModel viewModel;

    UserInfoThisEditActBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.user_info_this_edit_act);
        viewModel = new UserInfoEditViewModel(this);
        binding.setViewModel(viewModel);
        setupActionBar();
    }

    //刷新一下数据
    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.setNavigator(this);
            viewModel.start();
        }
    }


    public void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(true);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        viewModel.setSchool(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void setupSpinner(List<School> data, int currentSchoolId) {
        runOnUiThread(() -> {
            ArrayAdapter<School> arrayAdapter = new ArrayAdapter<School>(this, android.R.layout.simple_spinner_dropdown_item, data);
            binding.spinner.setAdapter(arrayAdapter);
            binding.spinner.setOnItemSelectedListener(this);
            binding.spinner.setSelection(currentSchoolId - 1);
        });
    }

    @Override
    public void success(String result) {

    }

    @Override
    public void failed(String result) {

    }

    @Override
    public void backToUserInfoAct(int result, String log) {
        runOnUiThread(() -> {
            Intent intent = new Intent();
            intent.putExtra("data_return", log);
            setResult(result, intent);
            finish();
        });
    }

    @Override
    public void showMessage(String msg) {
        runOnUiThread(() -> {
            ToastUtil.showShort(getBaseContext(), msg);
        });
    }
}
