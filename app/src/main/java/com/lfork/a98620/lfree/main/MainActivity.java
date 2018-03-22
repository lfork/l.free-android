package com.lfork.a98620.lfree.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lfork.a98620.lfree.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login_act);
        //登录界面部分界面操作需要在这里控制后才能添加
    }
}
