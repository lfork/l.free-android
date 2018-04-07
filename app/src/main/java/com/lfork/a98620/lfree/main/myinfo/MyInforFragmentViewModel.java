package com.lfork.a98620.lfree.main.myinfo;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.LayoutInflater;

import com.lfork.a98620.lfree.BaseViewModel;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.UserDataRepository;
import com.lfork.a98620.lfree.databinding.MainMyInforFragBinding;
import com.lfork.a98620.lfree.login.LoginActivity;
import com.lfork.a98620.lfree.mygoods.MyGoodsActivity;
import com.lfork.a98620.lfree.settings.SettingsActivity;
import com.lfork.a98620.lfree.userinfothis.UserInfoThisActivity;

/**
 *
 * Created by 98620 on 2018/4/5.
 */

public class MyInforFragmentViewModel extends BaseViewModel {

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> description = new ObservableField<>();

    public final ObservableField<String> imageUrl = new ObservableField<>();


    MyInforFragmentViewModel(MainMyInforFragBinding binding, Context context, LayoutInflater layoutInflater) {
        super(context);
        initData();
    }

    private void initData(){
        User user = UserDataRepository.getInstance().getThisUser();
        username.set(user.getUserName());
        description.set(user.getUserDesc());
        imageUrl.set(user.getUserImagePath());
    }

    public void onQuit(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra("status", LoginActivity.LOGOUT);

        UserDataRepository.destroyInstance();
        getContext().startActivity(intent);
        // 资源释放。。。

    }

    public void openSettings(){
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        getContext().startActivity(intent);


    }

    public void openUserInfoDetail(){
        Intent intent = new Intent(getContext(), UserInfoThisActivity.class);
        getContext().startActivity(intent);


    }

    public void openMyGoods(){
        Intent intent = new Intent(getContext(), MyGoodsActivity.class);
        getContext().startActivity(intent);


    }
}
