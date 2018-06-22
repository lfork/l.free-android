package com.lfork.a98620.lfree.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.databinding.LoginActBinding;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.register.RegisterActivity;
import com.lfork.a98620.lfree.util.GlideOptions;

public class LoginActivity extends AppCompatActivity {

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> password = new ObservableField<>();

    public final ObservableBoolean isLogging = new ObservableBoolean(false);

    public final static int LOGOUT = 2;

    private static final String TAG = "LoginActivity";


    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActBinding binding = DataBindingUtil.setContentView(this, R.layout.login_act);
        binding.setViewModel(this);
        RequestOptions options = GlideOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true);
        ImageView loginLogo = findViewById(R.id.login_logo);
        Glide.with(this).load(R.drawable.login_logo)
                .apply(options)
                .into(loginLogo);
        initLoginStatus();
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
        showLoginDialog();
        user.setUserName(username.get());
        user.setUserPassword(password.get());
        //合法检测
        if (!TextUtils.isEmpty(user.getUserName()) || !TextUtils.isEmpty(user.getUserPassword())) {

            new Thread(() -> UserDataRepository.getInstance().login(new DataSource.GeneralCallback<User>() {
                @Override
                public void succeed(User data) {
                    Log.d(TAG, "succeed: " + data);
                    showDealResult("登录成功" + data.toString());
                    saveLoginStatus(1);
                    closeLoginDialog();
                    startMainActivity();
                }

                @Override
                public void failed(String log) {
                    Log.d(TAG, "failed: " + log);
                    closeLoginDialog();
                    showDealResult("登录失败" + log);
                }
            }, user)).start();
        } else {
            showDealResult("输入的信息不能为空");
            closeLoginDialog();
        }


    }

    public void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void showDealResult(final String result) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show());
    }

    private void showLoginDialog() {
        isLogging.set(true);
    }

    private void closeLoginDialog() {
        isLogging.set(false);

    }


    /**
     * 用户启动后对登录状态进行判断
     */
    private void initLoginStatus() {
        SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);
        username.set(pref.getString("username", ""));
        password.set(pref.getString("password", ""));

        Intent intent = getIntent();
        int status = intent.getIntExtra("status", -1);

        if (status == LOGOUT) {
            saveLoginStatus(status);
            return;
        }

        status = pref.getInt("status", 0);
        int LOGIN = 1;
        if (status == LOGIN) {
            autoLogin();
        }
    }

    /**
     * 检测用户是否已经登录，如果已经登录那么就不用再次登录了，而是直接使用已有的登录信息
     */
    private void autoLogin() {
        startMainActivity();
    }


    private void saveLoginStatus(int status) {
        SharedPreferences.Editor editor = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
        editor.putInt("status", status);
        editor.putString("username", username.get());
        editor.putString("password", password.get());
        editor.apply();
    }
}
