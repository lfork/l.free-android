package com.lfork.a98620.lfree.main.myinfo;

import android.text.TextUtils;
import android.util.Log;

import com.lfork.a98620.lfree.base.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.util.Config;

import static android.content.ContentValues.TAG;

/**
 * Created by 98620 on 2018/4/5.
 */

public class MyInfoFragmentViewModel extends UserViewModel {

    private MyInfoFragmentNavigator navigator;

    private User user;

    MyInfoFragmentViewModel(MainActivity context) {
        super(context);
    }

    private void getUserInfo() {
        UserDataRepository repository = UserDataRepository.getInstance();
        user = repository.getThisUser();
        if (user == null)
            new Thread(() -> repository.getThisUser(new DataSource.GeneralCallback<User>() {
                @Override
                public void succeed(User user) {
                    setUser(user);
                }

                @Override
                public void failed(String log) {
                    Log.d(TAG, "failed: " + log);
                }
            })).start();
        else {
            setUser(user);
        }

    }

    private void setUser(User user){
        username.set(user.getUserName());
        if (TextUtils.isEmpty(user.getUserDesc())) {
            description.set("该用户还没有自我介绍....");
        } else {
            description.set(user.getUserDesc());
        }
        imageUrl.set(Config.ServerURL + "/image" + user.getUserImagePath());

    }

    public void onQuit() {
        if (navigatorIsNotNull()) {
            navigator.logoff();
            new Thread(() -> {
                user.setLogin(false);
                user.save();
            }).start();
        }

    }

    public void openSettings() {
        if(navigatorIsNotNull()){
            navigator.openSettings();
        }
    }

    public void openUserInfoDetail() {
       if(navigatorIsNotNull()){
           navigator.openUserInfoDetail();
       }

    }

    public void openMyGoods() {
        if(navigatorIsNotNull()){
            navigator.openMyGoods();
        }
    }

    private boolean navigatorIsNotNull(){
        return navigator != null;

    }

    public void setNavigator(MyInfoFragmentNavigator navigator) {
        super.setNavigator(navigator);
        this.navigator = navigator;
    }

    @Override
    public void start() {
        getUserInfo();
    }

    @Override
    public void onDestroy() {
        navigator = null;
    }
}
