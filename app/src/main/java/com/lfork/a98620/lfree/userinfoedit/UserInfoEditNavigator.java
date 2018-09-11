package com.lfork.a98620.lfree.userinfoedit;

import com.lfork.a98620.lfree.data.base.entity.School;

import java.util.List;

/**
 * Created by 98620 on 2018/6/13.
 */
public interface UserInfoEditNavigator {
    void setupSpinner(List<School> data, int currentSchoolId);

    void success(String result);

    void failed(String result);

    void backToUserInfoAct(int result, String log);

    void showMessage(String msg);
}
