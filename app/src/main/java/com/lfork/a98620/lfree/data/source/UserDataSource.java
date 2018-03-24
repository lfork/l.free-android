package com.lfork.a98620.lfree.data.source;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;

/**
 * Created by 98620 on 2018/3/23.
 **/

public interface UserDataSource extends DataSource {
    void login(GeneralCallback<User> callback, User user);

    void register(GeneralCallback<String> callback, User user);

    User getThisUser();

    void updateThisUser();
}
