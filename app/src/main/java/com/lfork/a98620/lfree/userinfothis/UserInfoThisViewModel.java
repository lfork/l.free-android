package com.lfork.a98620.lfree.userinfothis;

import android.databinding.ObservableBoolean;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.lfork.a98620.lfree.base.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.util.Config;

/**
 * 两个activity使用同一个类型的viewModel，但是里面的方法确不是通用的
 * Created by 98620 on 2018/4/9.
 */

public class UserInfoThisViewModel extends UserViewModel {

    private static final String TAG = "UserInfoEditViewModel";

    private User user;
    private UserDataRepository repository;

    public final ObservableBoolean isUpdating = new ObservableBoolean(false);

    private ViewModelNavigator navigator;


    UserInfoThisViewModel(AppCompatActivity context) {
        super(context);
    }

    @Override
    public void start() {
        getUserInfo();
    }

    private void getUserInfo() {
        new Thread(() -> {
            repository = UserDataRepository.getInstance();
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
                    if (user.getSchool() == null) {
                        school.set("未知");
                    } else {
                        school.set(user.getSchool().getSchoolName());
                    }

                }

                @Override
                public void failed(String log) {
                    showMessage(log);
                }
            }, repository.getUserId());

        }).start();

    }

    public void updateUserPortrait(String localFilePath) {
        isUpdating.set(true);
        new Thread(() -> {
            repository.updateUserPortrait(new DataSource.GeneralCallback<String>() {
                @Override
                public void succeed(String data1) {

                    imageUrl.set(Config.ServerURL + "/image" + data1);
                    showMessage("更新成功");
                }

                @Override
                public void failed(String log) {
                    isUpdating.set(false);
                    showMessage(log);
                }
            }, studentNumber.get(), localFilePath);

        }).start();

    }

    private void showMessage(String msg){
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
