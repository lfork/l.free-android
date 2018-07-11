package com.lfork.a98620.lfree.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.LoginActBinding;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.register.RegisterActivity;
import com.lfork.a98620.lfree.util.GlideOptions;
import com.lfork.a98620.lfree.util.ToastUtil;

/**
 * @author 98620
 */
public class LoginActivity extends AppCompatActivity implements LoginNavigator {
    /**
     * 退出登录启动
     */
    public static final int LAUNCH_QUITE = 1;

    /**
     * 登录状态
     */
    public final static int STATUS_LOGIN = 0;

    public final static int STATUS_LOGOUT = 1;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActBinding binding = DataBindingUtil.setContentView(this, R.layout.login_act);
        viewModel = new LoginViewModel(this);
        viewModel.setNavigator(this);
        binding.setViewModel(viewModel);
        Intent intent = getIntent();
        int launchMode = intent.getIntExtra("launch_mode", 0);

        if (launchMode == LAUNCH_QUITE) {
            viewModel.setLoginInfo("", STATUS_LOGOUT);
        }
        RequestOptions options = GlideOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true);
        ImageView loginLogo = findViewById(R.id.login_logo);
        Glide.with(this).load(R.drawable.login_logo)
                .apply(options)
                .into(loginLogo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }


    @Override
    public void loginSucceed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailed(String log) {
        showToast(log);
    }

    @Override
    public void openRegisterAct() {
        RegisterActivity.start(this);
    }

    @Override
    public void openForgetPasswordAct() {
        showToast("未实现");
    }
//
//    public void login() {
//        showLoginDialog();
//        user.setUserName(username.get());
//        user.setUserPassword(password.get());
//        //合法检测
//        if (!TextUtils.isEmpty(user.getUserName()) || !TextUtils.isEmpty(user.getUserPassword())) {
//
//            new Thread(() -> UserDataRepository.getInstance().login(new DataSource.GeneralCallback<User>() {
//                @Override
//                public void succeed(User data) {
//                    showDealResult("登录成功"); //+ data.toString()
//                    saveLoginStatus(1);
//                    closeLoginDialog();
//                    startMainActivity();
//                }
//
//                @Override
//                public void failed(String log) {
//                    closeLoginDialog();
//                    showDealResult("登录失败" + log);
//                }
//            }, user)).start();
//        } else {
//            showDealResult("输入的信息不能为空");
//            closeLoginDialog();
//        }
//
//
//    }
//
//    private void showDealResult(final String result) {
//        runOnUiThread(() -> Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show());
//    }
//
//    private void showLoginDialog() {
//        dataIsLoading.set(true);
//    }
//
//    private void closeLoginDialog() {
//        dataIsLoading.set(false);
//    }
//
//
//    /**
//     * 用户启动后对登录状态进行判断
//     */
//    private void judgeLoginStatus() {
//        SharedPreferences pref = getSharedPreferences("loginInfo", MODE_PRIVATE);
//        username.set(pref.getString("username", ""));
//        password.set(pref.getString("password", ""));
//
//        Intent intent = getIntent();
//        int status = intent.getIntExtra("status", -1);
//
//        if (status == STATUS_LOGOUT) {
//            saveLoginStatus(status);
//            return;
//        }
//
//        status = pref.getInt("status", 0);
//        int LOGIN = 1;
//        if (status == LOGIN) {
//            autoLogin();
//        }
//    }
//
//    /**
//     * 检测用户是否已经登录，如果已经登录那么就不用再次登录了，而是直接使用已有的登录信息
//     */
//    private void autoLogin() {
//        startMainActivity();
//    }
//
//
//    private void saveLoginStatus(int status) {
//        SharedPreferences.Editor editor = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();
//        editor.putInt("status", status);
//        editor.putString("username", username.get());
//        editor.putString("password", password.get());
//        editor.apply();
//    }

    @Override
    public void showToast(String msg) {
        runOnUiThread(() -> ToastUtil.showShort(LoginActivity.this, msg));
    }

    public static void start(Context context, int launchMode) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("launch_mode", launchMode);
        context.startActivity(intent);
    }
}
