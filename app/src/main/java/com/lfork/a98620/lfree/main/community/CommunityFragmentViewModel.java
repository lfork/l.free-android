package com.lfork.a98620.lfree.main.community;

import android.content.Context;
import android.util.Log;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.community.remote.CommunityRemoteDataSource;

import static com.yalantis.ucrop.UCropFragment.TAG;

public class CommunityFragmentViewModel extends BaseViewModel {

    @Override
    public void start() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    CommunityFragmentViewModel(Context context) {
        super(context);
    }

    public void loadData(String fromTime, CommunityCallback callback) {
        Log.d(TAG, "loadData: 我要加载数据了");
        new Thread(() -> {
            CommunityRemoteDataSource.getINSTANCE().getArticleList(fromTime, new DataSource.GeneralCallback() {
                @Override
                public void succeed(Object data) {
                    Log.d(TAG, "succeed: 我加载成功了，马上回调了");
                    callback.callback(data, 1);
                }

                @Override
                public void failed(String log) {
                    Log.d(TAG, "failed: " + log);
                    Log.d(TAG, "failed: 我加载失败了，还是回调吧");
                    callback.callback(0, 2);
                }
            });
        }).start();
    }
}
