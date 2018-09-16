package com.lfork.a98620.lfree.register;

import android.databinding.ObservableField;
import android.util.Log;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.School;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.util.UserValidation;

import java.util.List;

/**
 * Created by 98620 on 2018/6/12.  因为注册模块比较简单，暂时就不写viewModel了
 */
public class RegisterInfoViewModel {

    private static final String TAG = "RegisterInfoViewModel";

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> studentId = new ObservableField<>();

    public final ObservableField<String> password = new ObservableField<>();

    public final ObservableField<String> passwordConfirm = new ObservableField<>();

    public final ObservableField<String> school = new ObservableField<>("10");

    private User user = new User();

    private UserDataRepository repository;

    private List<School> schools;

    private RegisterNavigator navigator;

    RegisterInfoViewModel() {
    }

    public void start() {
        getSchoolList();
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
                    navigator.setupSpinner(data);
                }
            }

            @Override
            public void failed(String log) {

            }
        });
    }

    public void register() {
        user.setStudentId(studentId.get());
        user.setUserName(username.get());
        user.setUserPassword(password.get());
        user.setUserSchool(new School(school.get(),""));

        String result = dataValidate();  //等于null，说明注册成功
        if (result == null) {
            repository.register(new DataSource.GeneralCallback<String>() {
                @Override
                public void succeed(String data) {
                    if (navigator != null) {
                        navigator.success("注册成功, 请牢记你的账号:" + data);
                    }
                }

                @Override
                public void failed(String log) {
                    if (navigator != null) {
                        navigator.failed("注册失败:" + log);
                    }

                }
            }, user);
        } else {
            if (navigator != null) {
                navigator.failed(result);
            }
        }
    }

    private String dataValidate() {
        //Log.d(TAG, "validate s: " + user.getStudentId() + " u:" + user.getUserName() + " p:" + user.getUserPassword());
        return UserValidation.RegisterValidation(studentId.get(), username.get(), password.get(), passwordConfirm.get());
    }

    public void setNavigator(RegisterNavigator navigator) {
        this.navigator = navigator;
    }


    public void onDestroy() {
        navigator = null;
    }

    public void setSchool(int position) {
        Log.d(TAG, "succeed: " + position + " " + schools.get(position));
        school.set(schools.get(position).getId());
    }
}
