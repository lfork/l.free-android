package com.lfork.a98620.lfree.userinfothis;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lfork.a98620.lfree.base.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.util.Config;
import com.lfork.a98620.lfree.util.StringUtil;
import com.lfork.a98620.lfree.util.ToastUtil;

/**
 * 两个activity使用同一个类型的viewModel，但是里面的方法确不是通用的
 * Created by 98620 on 2018/4/9.
 */

public class UserInfoThisViewModel extends UserViewModel {

    private User user;
    private UserDataRepository repository;
    private AppCompatActivity context;
    public ObservableBoolean isUpdating = new ObservableBoolean(false);


    UserInfoThisViewModel(AppCompatActivity context) {
        super(context);
        this.context = context;
    }

    void refreshData() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                repository = UserDataRepository.getInstance();
                user = repository.getThisUser();
                if (user != null) {
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
                } else {
                    ToastUtil.showShort(context, "可能没有网络");
                }
            }
        }).start();

    }

    void updateUserPortrait(String localFilePath) {
        isUpdating.set(true);
        new Thread(() -> {
            repository.updateUserPortrait(new DataSource.GeneralCallback<String>() {
                @Override
                public void succeed(String data1) {
                    user.setUserImagePath(data1);
                    repository.saveThisUser(user);
                    imageUrl.set(Config.ServerURL + "/image" + user.getUserImagePath());
                    context.runOnUiThread(() -> {
                        Toast.makeText(context, "更新成功", Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void failed(String log) {
                    isUpdating.set(false);
                    context.runOnUiThread(() -> {
                        Toast.makeText(context, log, Toast.LENGTH_LONG).show();
                    });
                }
            }, studentNumber.get(), localFilePath);

        }).start();

    }

    /**
     * 下面的操作是给edit界面使用的
     */
    void updateUserInfo() {
        //先搞一下异步操作，如果搞完以后是更新成功的话就重置本地信息，如果不是的话就不重置用户信息
        User newUser = new User();
        newUser.setUserId(user.getUserId());
        newUser.setUserPhone(phone.get());
        newUser.setUserName(user.getUserName());
        newUser.setUserEmail(email.get());
        newUser.setUserDesc(description.get());

        isUpdating.set(true);
        new Thread(() -> {
            repository.updateThisUser(new DataSource.GeneralCallback<String>() {
                @Override
                public void succeed(String data) {
                    context.runOnUiThread(() -> {
                        isUpdating.set(false);
                        //跳转到详细信息的界面
                        backToUserInfoAct(Activity.RESULT_OK, data);
                    });
                }

                @Override
                public void failed(String log) {
                    context.runOnUiThread(() -> {
                        isUpdating.set(false);
                        //跳转到详细信息的界面
                        backToUserInfoAct(Activity.RESULT_CANCELED, log);
                    });

                }
            }, newUser);
        }).start();
    }

    private void backToUserInfoAct(int result, String log) {
        Intent intent = new Intent();
        intent.putExtra("data_return", log);
        context.setResult(result, intent);
        context.finish();
    }

    @Override
    public void start() {

    }

    @Override
    public void destroy() {

    }
}
