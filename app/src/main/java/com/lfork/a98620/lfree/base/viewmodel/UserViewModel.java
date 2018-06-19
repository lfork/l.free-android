package com.lfork.a98620.lfree.base.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.base.BaseViewModel;

/**
 * Created by 98620 on 2018/4/9.
 */

public abstract class UserViewModel extends BaseViewModel {

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> description = new ObservableField<>("");

    public final ObservableField<String> imageUrl = new ObservableField<>();

    public final ObservableField<String> studentNumber = new ObservableField<>("");

    public final ObservableField<String> phone = new ObservableField<>();

    public final ObservableField<String> email = new ObservableField<>("");

    public final ObservableField<String> gender = new ObservableField<>();

    public final ObservableField<String> school = new ObservableField<>();

    public UserViewModel() {
        super();
    }

    public UserViewModel(Context context) {
        super(context);

    }


}
