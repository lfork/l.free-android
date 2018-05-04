package com.lfork.a98620.lfree.userinfo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.lfork.a98620.lfree.base.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.util.Config;
import com.lfork.a98620.lfree.util.StringUtil;
import com.lfork.a98620.lfree.util.ToastUtil;

/**
 * Created by 98620 on 2018/4/13.
 */

public class UserInfoViewModel extends UserViewModel {

    private AppCompatActivity context;

    private int userId;

    UserInfoViewModel(Context context, int userId) {
        super(context);
        this.userId = userId;
        refreshData();
    }

    private void refreshData() {
        UserDataRepository repository = UserDataRepository.getInstance();
        new Thread(() -> {
            repository.getUserInfo(new DataSource.GeneralCallback<User>() {
                @Override
                public void succeed(User data) {
                    setData(data);
                }

                @Override
                public void failed(String log) {
                    context.runOnUiThread(() -> {
                        ToastUtil.showShort(context, log);
                    });
                }
            }, userId);

        }).start();
    }

    private void setData(User user) {
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
        getNavigator().setParam1(username.get());
    }

}
