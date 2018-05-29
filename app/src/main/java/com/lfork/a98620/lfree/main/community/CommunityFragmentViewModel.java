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
    private List<CommunityArticle> itemViewModelList;

    CommunityFragmentViewModel(Activity context) {
        this.context = context;
    }

    public void loadData(CommunityCallback callback, boolean isRefresh) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                CommunityRemoteDataSource.getINSTANCE().getArticleList(new DataSource.GeneralCallback() {
                    @Override
                    public void succeed(Object data) {
                        itemViewModelList = (List<CommunityArticle>) data;
                        if (isRefresh) {
                            Log.d(TAG, "succeed: 刷新成功，马上回调");
                            callback.callback(itemViewModelList, 3);
                        } else {
                            Log.d(TAG, "succeed: 加载成功，马上回调");
                            callback.callback(itemViewModelList, 1);
                        }
                    }

                    @Override
                    public void failed(String log) {
                        Log.d(TAG, "failed: " + log);
                        Log.d(TAG, "succeed: 加载失败，马上回调");
                        callback.callback(null, 2);
                    }
                });
            }
        }).start();
    }
}
