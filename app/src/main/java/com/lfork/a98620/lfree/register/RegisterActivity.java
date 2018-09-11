package com.lfork.a98620.lfree.register;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.base.entity.School;
import com.lfork.a98620.lfree.databinding.RegisterActBinding;

import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,RegisterNavigator{
    private static final String TAG = "RegisterActivity";
    private RegisterActBinding binding;
    private RegisterInfoViewModel viewModel;

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.setNavigator(this);
            viewModel.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.register_act);
        viewModel = new RegisterInfoViewModel();
        binding.setViewModel(viewModel);
        setupActionBar();
    }


    public void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(true);
        actionBar.setTitle("用户注册");
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setupSpinner(List<School> data) {
        runOnUiThread(() -> {
            ArrayAdapter<School> arrayAdapter = new ArrayAdapter<School>(this, android.R.layout.simple_spinner_dropdown_item,data);
            binding.spinner.setAdapter(arrayAdapter);
            binding.spinner.setOnItemSelectedListener(this);
        });
    }


    @Override
    public void success(final String result) {
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            finish();
        });
    }

    @Override
    public void failed(String result) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       viewModel.setSchool(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    public static void start(Context activityContext){
        Intent intent = new Intent(activityContext, RegisterActivity.class);
        activityContext.startActivity(intent);
    }
}
