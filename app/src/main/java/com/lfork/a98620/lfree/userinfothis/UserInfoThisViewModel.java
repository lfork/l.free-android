package com.lfork.a98620.lfree.userinfothis;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.common.viewmodel.UserViewModel;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.UserDataRepository;
import com.lfork.a98620.lfree.util.StringUtil;

/**
 * Created by 98620 on 2018/4/9.
 */

public class UserInfoThisViewModel extends UserViewModel{


    public void updateUserInfo(){
    }

    void refreshData(){
        UserDataRepository repository = UserDataRepository.getInstance();
        User user = repository.getThisUser();
        username.set(user.getUserName());
        if (StringUtil.isNull(user.getUserDesc())){
            description.set("该用户还没有自我介绍....");
        } else {
            description.set(user.getUserDesc());
        }

        imageUrl.set(user.getUserImagePath());
        email.set(user.getUserEmail());
        phone.set(user.getUserPhone());
        studentNumber.set(user.getStudentId());



    }

    public UserInfoThisViewModel(Context context) {
        super(context);
    }
}
