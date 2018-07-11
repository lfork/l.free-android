package com.lfork.a98620.lfree.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.base.FreeApplication;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;

/**
 * @author 98620
 * @date 2018/7/11
 */
public class LoginViewModel extends BaseViewModel {
    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> password = new ObservableField<>();

    public final ObservableBoolean isLogging = new ObservableBoolean(false);

    private Context context;

    /**
     * @param context 用来配置SharedPreferences
     */
    LoginViewModel(Context context) {
        super(context);
        this.context = context;
    }

    private LoginNavigator navigator;

    private UserDataRepository repository;

    @Override
    public void start() {
        super.start();
        this.isLogging.set(false);
        repository = UserDataRepository.getInstance();
        getLoginInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigator = null;
        repository = null;
        context = null;
    }

    /**
     * 获取判断最近的登录信息 ，如果已经登录的话就直接启动主界面了
     *
     * 这里直接与二号数据仓库(SharedPreferences 进行通信了)
     */
    private void getLoginInfo() {
        if (context == null) {
            return;
        }

        String recentUserId = getRecentUserId();
        if (TextUtils.isEmpty(recentUserId)) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences("login_info_" + recentUserId, Context.MODE_PRIVATE);
        int status = pref.getInt("status", -1);
        if (status == LoginActivity.STATUS_LOGIN) {
            if (navigator != null) {
                //直接登录成功
                navigator.loginSucceed();
            }
            return;
        }
        username.set(pref.getString("username", ""));
        password.set(pref.getString("password", ""));
    }

    /**
     * 当退出登录或者是登录成功后需要设置当前账号的登录状态，保存在SharedPreferences里面
     *
     * @param status {@link LoginActivity#STATUS_LOGIN} {@link LoginActivity#STATUS_LOGOUT  }
     * @param userId id
     */
    public void setLoginInfo(String userId, int status) {
        if (context == null) {
            return;
        }
        String recentUserId = null;
        if (TextUtils.isEmpty(userId)) {
            recentUserId = getRecentUserId();
            if (TextUtils.isEmpty(recentUserId)) {
                return;
            }
            userId = recentUserId;
        } else {
            setRecentUserId(userId);
        }

        SharedPreferences.Editor editor = context.getSharedPreferences("login_info_" + userId, Context.MODE_PRIVATE).edit();
        editor.putInt("status", status);
        if (!TextUtils.isEmpty(username.get())){
            editor.putString("username", username.get());
        }

        if (!TextUtils.isEmpty(password.get())){
            editor.putString("password", password.get());
        }


        editor.apply();
    }

    private String getRecentUserId() {
        if (context == null) {
            return null;
        }
        SharedPreferences appPref = context.getApplicationContext().getSharedPreferences(FreeApplication.APP_SHARED_PREF, Context.MODE_PRIVATE);
        return appPref.getString("recent_user_id", "");
    }

    private void setRecentUserId(String recentUserId) {
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(FreeApplication.APP_SHARED_PREF, Context.MODE_PRIVATE).edit();
        editor.putString("recent_user_id", recentUserId);
        editor.apply();
    }


    public void login() {
        User user = new User();
        user.setUserName(username.get());
        user.setUserPassword(password.get());

        if (TextUtils.isEmpty(user.getUserName()) || TextUtils.isEmpty(user.getUserPassword())) {
            showToast("输入的信息不能为空");
            return;
        }

        isLogging.set(true);
        repository.login(new DataSource.GeneralCallback<User>() {
            @Override
            public void succeed(User data) {

                if (checkNavigator()) {
                    return;
                }
                setLoginInfo(data.getUserId() + "", LoginActivity.STATUS_LOGIN);
                showToast("登录成功");
                navigator.loginSucceed();
                isLogging.set(false);
            }

            @Override
            public void failed(String log) {
                isLogging.set(false);
                if (checkNavigator()) {
                    return;
                }
                navigator.loginFailed(log);
            }
        }, user);
    }

    public void register() {
        if (checkNavigator()) {
            return;
        }
        navigator.openRegisterAct();
    }

    public void forgetPassword() {
        if (checkNavigator()) {
            return;
        }
        navigator.openForgetPasswordAct();
    }

    public void setNavigator(LoginNavigator navigator) {
        super.setNavigator(navigator);
        this.navigator = navigator;
    }

    private boolean checkNavigator() {
        return navigator == null;
    }
}
