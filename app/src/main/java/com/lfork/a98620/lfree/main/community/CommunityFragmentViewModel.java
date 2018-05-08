package com.lfork.a98620.lfree.main.community;

import android.app.Activity;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.community.remote.CommunityRemoteDataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;

import java.util.ArrayList;
import java.util.List;

import static com.yalantis.ucrop.UCropFragment.TAG;

public class CommunityFragmentViewModel extends BaseViewModel {
    private Activity context;
    private List<CommunityArticle> articleList;

    CommunityFragmentViewModel(Activity context) {
        this.context = context;
    }

    public void start(CommunityCallback callback){
        loadData(callback);
    }

    private void loadData(CommunityCallback callback) {
        CommunityRemoteDataSource.getINSTANCE().getArticleList(new DataSource.GeneralCallback() {
            @Override
            public void succeed(Object data) {
                articleList = (List<CommunityArticle>) data;
                callback.callback(articleList);
            }

            @Override
            public void failed(String log) {
                Log.d(TAG, "failed: " + log);
                callback.callback(null);
            }
        });
    }
}
