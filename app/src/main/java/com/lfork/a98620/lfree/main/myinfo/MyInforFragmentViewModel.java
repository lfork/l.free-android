package com.lfork.a98620.lfree.main.myinfo;

import android.content.Intent;
import android.util.Log;

import com.lfork.a98620.lfree.base.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.login.LoginActivity;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.mygoods.MyGoodsActivity;
import com.lfork.a98620.lfree.settings.SettingsActivity;
import com.lfork.a98620.lfree.userinfothis.UserInfoThisActivity;
import com.lfork.a98620.lfree.util.Config;
import com.lfork.a98620.lfree.util.StringUtil;

import static android.content.ContentValues.TAG;

/**
 * Created by 98620 on 2018/4/5.
 */

public class MyInforFragmentViewModel extends UserViewModel {

    private MainActivity context;

    private User user;

    MyInforFragmentViewModel(MainActivity context) {
        super(context);
        this.context = context;
        refreshData();
    }

    void refreshData() {
        UserDataRepository repository = UserDataRepository.getInstance();
        user = repository.getThisUser();
        if (user == null)
            new Thread(() -> repository.getThisUser(new DataSource.GeneralCallback<User>() {
                @Override
                public void succeed(User user) {
                    initData(user);
                }

                @Override
                public void failed(String log) {
                    Log.d(TAG, "failed: " + log);
                }
            })).start();
        else {
            initData(user);
        }

    }

    private void initData(User user){
        username.set(user.getUserName());
        if (StringUtil.isNull(user.getUserDesc())) {
            description.set("该用户还没有自我介绍....");
        } else {
            description.set(user.getUserDesc());
        }
        imageUrl.set(Config.ServerURL + "/image" + user.getUserImagePath());

    }

    public void onQuit() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("status", LoginActivity.LOGOUT);
        context.startActivity(intent);

        new Thread(() -> {
            user.setLogin(false);
            user.save();
        }).start();
        context.finish();
        // 资源释放。。。

    }

    public void openSettings() {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);


    }

    public void openUserInfoDetail() {
        Intent intent = new Intent(context, UserInfoThisActivity.class);
        context.startActivity(intent);

    }

    public void openMyGoods() {
        Intent intent = new Intent(context, MyGoodsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }
}
