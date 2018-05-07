package com.lfork.a98620.lfree.main.community;

import android.app.Activity;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.lfork.a98620.lfree.base.BaseViewModel;

import java.util.List;

public class CommunityFragmentViewModel extends BaseViewModel {
    private Activity context;


    CommunityFragmentViewModel(Activity context) {
        super(context);
        this.context = context;
    }

    public void start(){
        loadData();
    }

    private void loadData() {
    }
}
