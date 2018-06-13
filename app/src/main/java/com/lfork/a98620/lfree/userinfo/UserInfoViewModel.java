package com.lfork.a98620.lfree.userinfo;

import android.content.Context;

import com.lfork.a98620.lfree.base.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.util.Config;
import com.lfork.a98620.lfree.util.StringUtil;

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
        new Thread(() -> {
            repository.getUserInfo(new DataSource.GeneralCallback<User>() {
                @Override
                public void succeed(User user) {
                    username.set(user.getUserName());
                    if (StringUtil.isNull(user.getUserDesc())) {
                        description.set("该用户还没有自我介绍....");
                    } else {
                        description.set(user.getUserDesc());
                    }
                    imageUrl.set(Config.ServerURL + "/image" + user.getUserImagePath());
                    email.set(user.getUserEmail());
                    phone.set(user.getUserPhone());
                    studentNumber.set(user.getUserId() + "");
                    school.set(user.getSchool().getSchoolName());
                }
                @Override
                public void failed(String log) {
                    if (navigator != null) {
                        navigator.showMessage(log);
                    }
                }
            }, userId);

        }).start();
    }


    public int getUserId() {
        return userId;
    }



    @Override
    public void destroy() {
        navigator = null;
    }
}
