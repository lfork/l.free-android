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
