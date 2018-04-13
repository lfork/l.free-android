package com.lfork.a98620.lfree.userinfothis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.lfork.a98620.lfree.common.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.UserDataRepository;
import com.lfork.a98620.lfree.util.StringUtil;

/** 两个activity使用同一个类型的viewModel，但是里面的方法确不是通用的
 * Created by 98620 on 2018/4/9.
 */

public class UserInfoThisViewModel extends UserViewModel{

    private User user;
    private UserDataRepository repository;
    private AppCompatActivity context;
    public ObservableBoolean isUpdating = new ObservableBoolean(false);


    void refreshData(){
        repository = UserDataRepository.getInstance();
        user = repository.getThisUser();
        username.set(user.getUserName());
        if (StringUtil.isNull(user.getUserDesc())){
            description.set("该用户还没有自我介绍....");
        } else {
            description.set(user.getUserDesc());
        }
        imageUrl.set(user.getUserImagePath());
        email.set(user.getUserEmail());
        phone.set(user.getUserPhone());
        studentNumber.set(user.getUserId() + "");
    }

    UserInfoThisViewModel(AppCompatActivity context) {
        super(context);
        this.context = context;
    }

    void updateUserPortrait(){

    }

    /**
     * 下面的操作是给edit界面使用的
     */
    void updateUserInfo(){
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
                public void success(String data) {
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

    private void backToUserInfoAct(int result, String log){
        Intent intent= new Intent();
        intent.putExtra("data_return", log);
        context.setResult(result, intent);
        context.finish();
    }
}
