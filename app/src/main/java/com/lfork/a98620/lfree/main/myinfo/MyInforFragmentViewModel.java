package com.lfork.a98620.lfree.main.myinfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import com.lfork.a98620.lfree.BaseViewModel;
import com.lfork.a98620.lfree.databinding.MainMyInforFragBinding;
import com.lfork.a98620.lfree.login.LoginActivity;

/**
 *
 * Created by 98620 on 2018/4/5.
 */

public class MyInforFragmentViewModel extends BaseViewModel {


    MyInforFragmentViewModel(MainMyInforFragBinding binding, Context context, LayoutInflater layoutInflater) {
        super(context);
    }

    public void onQuit(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra("status", LoginActivity.LOGOUT);
        getContext().startActivity(intent);
    }
}
