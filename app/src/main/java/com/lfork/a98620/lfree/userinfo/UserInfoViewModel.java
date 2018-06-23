package com.lfork.a98620.lfree.userinfo;

import android.content.Context;
import android.text.TextUtils;

import com.lfork.a98620.lfree.base.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.util.Config;

/**
 * Created by 98620 on 2018/4/13.
 */

public class UserInfoViewModel extends UserViewModel {


    private ViewModelNavigator navigator;

    private int userId;

    UserInfoViewModel(Context context, int userId) {
        super(context);
        this.userId = userId;
    }

    @Override
    public void start() {
        getUserInfo();
    }

    private void getUserInfo() {
        UserDataRepository repository = UserDataRepository.getInstance();
        new Thread(() -> repository.getUserInfo(new DataSource.GeneralCallback<User>() {
            @Override
            public void succeed(User user) {
                username.set(user.getUserName());
                if (TextUtils.isEmpty(user.getUserDesc())) {
                    description.set("该用户还没有自我介绍....");
                } else {
                    description.set(user.getUserDesc());
                }
                imageUrl.set(Config.ServerURL + "/image" + user.getUserImagePath());
                email.set(user.getUserEmail());
                phone.set(user.getUserPhone());
                studentNumber.set(user.getUserId() + "");

                if (user.getSchool() == null) {
                    school.set("cuit");
                } else {
                    school.set(user.getSchool().getSchoolName());
                }


            }
            @Override
            public void failed(String log) {
                if (navigator != null) {
                    navigator.showToast(log);
                }
            }
        }, userId)).start();
    }


    public int getUserId() {
        return userId;
    }



    @Override
    public void onDestroy() {
        navigator = null;
    }
}
