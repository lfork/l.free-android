package com.lfork.a98620.lfree.register;

import com.lfork.a98620.lfree.data.base.entity.School;

import java.util.List;

/**
 * Created by 98620 on 2018/6/13.
 */
public interface RegisterNavigator {
    void setupSpinner(List<School> data);

    void success(String result);

    void failed(String result);

}
