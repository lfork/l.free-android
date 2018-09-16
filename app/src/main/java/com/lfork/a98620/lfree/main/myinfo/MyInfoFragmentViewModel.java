package com.lfork.a98620.lfree.main.myinfo;

import android.text.TextUtils;

import com.lfork.a98620.lfree.base.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.main.MainActivity;
import com.lfork.a98620.lfree.base.Config;

/**
 * Created by 98620 on 2018/4/5.
 */

public class MyInfoFragmentViewModel extends UserViewModel {

    private MyInfoFragmentNavigator navigator;

    MyInfoFragmentViewModel(MainActivity context) {
        super(context);
    }

    private void getUserInfo() {
        UserDataRepository repository = UserDataRepository.INSTANCE;
        repository.getUserInfo(new DataSource.GeneralCallback<User>() {
            @Override
            public void succeed(User data) {
                setUser(data);
            }
            @Override
            public void failed(String log) {
                showToast(log);
            }
        },repository.getUserId());

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
        super.onDestroy();
        navigator = null;
    }
}
