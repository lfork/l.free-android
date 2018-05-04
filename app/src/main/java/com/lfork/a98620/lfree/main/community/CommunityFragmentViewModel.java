package com.lfork.a98620.lfree.main.community;

import android.databinding.ObservableField;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.main.MainActivity;

/**
 * Created by 98620 on 2018/4/5.
 */

public class CommunityFragmentViewModel extends BaseViewModel {

    private MainActivity context;
    public final ObservableField<String> text = new ObservableField<>();


    CommunityFragmentViewModel(MainActivity context) {
        super(context);
        text.set("社区模块");
        this.context = context;
    }

    public void start(){
        loadData();
    }

    private void loadData() {
    }

}
