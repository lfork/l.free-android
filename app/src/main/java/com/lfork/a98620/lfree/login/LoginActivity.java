package com.lfork.a98620.lfree.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.UserDataRepository;
import com.lfork.a98620.lfree.databinding.LoginActBinding;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.register.RegisterActivity;
import com.lfork.a98620.lfree.util.UserValidation;

public class LoginActivity extends AppCompatActivity {

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> password = new ObservableField<>();

    private static final String TAG = "LoginActivity";

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActBinding binding = DataBindingUtil.setContentView(this, R.layout.login_act);
        binding.setViewModel(this);

        ImageView loginLogo = findViewById(R.id.login_logo);
        Glide.with(this).load(getResources().getDrawable(R.drawable.login_logo))
                .into(loginLogo);
        Log.d(TAG, "onCreate:  image setted successfully");
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void forgetPassword() {
        Toast.makeText(this, "功能还未实现", Toast.LENGTH_SHORT).show();
    }

    public void login() {
        user.setUserName(username.get());
        user.setUserPassword(password.get());
        //合法检测
        if (validate(user.getUserName(), user.getUserPassword())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserDataRepository mRepository = UserDataRepository.getInstance();
                    mRepository.login(new DataSource.GeneralCallback<User>() {
                        @Override
                        public void success(User data) {
                            Log.d(TAG, "success: " + data);
                            showDealResult("登录成功" + data.toString());
                            startMainActivity();
                        }

                        @Override
                        public void failed(String log) {
                            Log.d(TAG, "failed: " + log);
                            showDealResult("登录失败" + log);
                        }
                    }, user);
                }
            }).start();

        } else {
            showDealResult("请输入10位的学号， 8位以上的密码");
        }


    }

    public void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void showDealResult(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 对输入数据的合法性进行检测
     */
    private boolean validate(String username, String password) {
        Log.d(TAG, "validate u: " + username + " p:" + password);
        return UserValidation.LoginValidation(username, password);
    }
}
