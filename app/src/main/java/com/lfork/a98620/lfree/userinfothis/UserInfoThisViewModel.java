package com.lfork.a98620.lfree.userinfothis;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.lfork.a98620.lfree.base.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.base.Config;

/**
 * 两个activity使用同一个类型的viewModel，但是里面的方法确不是通用的
 *
 * @author 98620
 * @date 2018/4/9
 */

public class UserInfoThisViewModel extends UserViewModel {

    private UserDataRepository repository;

    private ViewModelNavigator navigator;

    UserInfoThisViewModel(AppCompatActivity context) {
        super(context);
    }

    @Override
    public void start() {
        getUserInfo();
    }

    private void getUserInfo() {
        repository = UserDataRepository.INSTANCE;
        repository.getUserInfo(new DataSource.GeneralCallback<User>() {
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
                if (user.getUserSchool() == null) {
                    school.set("未知");
                } else {
                    school.set(user.getUserSchool().getSchoolName());
                }

            }
            @Override
            public void failed(String log) {
                showMessage(log);
            }
        }, repository.getUserId());


    }

    public void updateUserPortrait(String localFilePath) {
        repository.updateUserPortrait(new DataSource.GeneralCallback<String>() {
            @Override
            public void succeed(String data1) {

                imageUrl.set(Config.ServerURL + "/image" + data1);
                showMessage("更新成功");
            }

            @Override
            public void failed(String log) {
                showMessage(log);
            }
        }, studentNumber.get(), localFilePath);
    }

    private void showMessage(String msg) {
        if (navigator != null) {
            navigator.showToast(msg);
        }
    }

    public void setNavigator(ViewModelNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigator = null;
    }
}
