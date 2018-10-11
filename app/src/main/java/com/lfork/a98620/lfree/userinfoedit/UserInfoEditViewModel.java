package com.lfork.a98620.lfree.userinfoedit;

import android.app.Activity;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.lfork.a98620.lfree.base.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.School;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.base.Config;

import java.util.List;

/**
 * 两个activity使用同一个类型的viewModel，但是里面的方法确不是通用的
 *
 * @author 98620
 * @date 2018/4/9
 */

public class UserInfoEditViewModel extends UserViewModel {

    private static final String TAG = "UserInfoEditViewModel";

    private User mUser;
    private UserDataRepository repository;
    public final ObservableBoolean isUpdating = new ObservableBoolean(false);
    public final ObservableField<String> school = new ObservableField<>("10");

    private List<School> schools;

    private UserInfoEditNavigator navigator;


    UserInfoEditViewModel(AppCompatActivity context) {
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
                mUser = user;
                username.set(user.getUserName());

                description.set(user.getUserDesc());
                email.set(user.getUserEmail());
                phone.set(user.getUserPhone());
                imageUrl.set(Config.ServerURL + "/image" + user.getUserImagePath());


                studentNumber.set(user.getUserId() + "");
                school.set(user.getUserSchool().getSchoolName());
                getSchoolList();
            }

            @Override
            public void failed(String log) {
                if (navigator != null) {
                    navigator.failed(log);
                }
            }
        }, repository.getUserId());
    }


    /**
     * 下面的操作是给edit界面使用的
     */
    public void updateUserInfo() {
        //先搞一下异步操作，如果搞完以后是更新成功的话就重置本地信息，如果不是的话就不重置用户信息
        User newUser = new User();
        newUser.setUserId(mUser.getUserId());
        newUser.setUserPhone(phone.get());
        newUser.setUserName(mUser.getUserName());
        newUser.setUserEmail(email.get());
        newUser.setUserDesc(description.get());
        newUser.setUserSchool(mUser.getUserSchool());

        if (TextUtils.isEmpty(newUser.getUserDesc())) {
            showMessage("描述不能为空");
            return;
        }

        if (TextUtils.isEmpty(newUser.getUserEmail())) {
            showMessage("邮箱不能为空");
            return;
        }

        if (TextUtils.isEmpty(newUser.getUserPhone())) {
            showMessage("电话不能为空");
            return;
        }

        isUpdating.set(true);
        repository.updateUserInfo(new DataSource.GeneralCallback<User>() {
            @Override
            public void succeed(User data) {
                isUpdating.set(false);
                if (navigator != null) {
                    //跳转到详细信息的界面
                    navigator.backToUserInfoAct(Activity.RESULT_OK, "更新成功");
                }
            }

            @Override
            public void failed(@NonNull String log) {
                isUpdating.set(false);
                if (navigator != null) {
                    //跳转到详细信息的界面
                    navigator.backToUserInfoAct(Activity.RESULT_CANCELED, log);
                }
            }
        }, newUser);
    }


    private void showMessage(String msg) {
        if (navigator != null) {
            //跳转到详细信息的界面
            navigator.showMessage(msg);
        }
    }


    /**
     * 简单的获取学校信息
     */
    private void getSchoolList() {
        repository = UserDataRepository.INSTANCE;
        repository.getSchoolList(new DataSource.GeneralCallback<List<School>>() {
            @Override
            public void succeed(List<School> data) {
                //这里是主线程
                schools = data;
                Log.d(TAG, "succeed: " + data);
                if (navigator != null) {
                    navigator.setupSpinner(data, Integer.parseInt(mUser.getUserSchool().getId()));
                }
            }

            @Override
            public void failed(String log) {
                if (navigator != null) {
                    navigator.failed(log);
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigator = null;
    }

    public void setNavigator(UserInfoEditNavigator navigator) {
        this.navigator = navigator;
    }

    public void setSchool(int position) {
        Log.d(TAG, "succeed: " + position + " " + schools.get(position));
        mUser.setUserSchool(schools.get(position));
    }

}
