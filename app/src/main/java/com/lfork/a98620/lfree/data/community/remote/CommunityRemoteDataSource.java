package com.lfork.a98620.lfree.data.community.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.articlecontent.CommunityComment;
import com.lfork.a98620.lfree.data.community.CommunityDataSource;
import com.lfork.a98620.lfree.data.community.local.CommunityLocalDataSource;
import com.lfork.a98620.lfree.main.community.CommunityArticle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.yalantis.ucrop.UCropFragment.TAG;

public class CommunityRemoteDataSource implements CommunityDataSource {

    private static CommunityRemoteDataSource INSTANCE;

    public static CommunityRemoteDataSource getINSTANCE() {
        //if (INSTANCE == null) {
            return new CommunityRemoteDataSource();
        //} else {
            //return INSTANCE;
        //}
    }
    @Override
    public void getArticleList(GeneralCallback callback) {
        Request request = new Request.Builder().url("http://imyth.top:8080/community_server/getcommunityarticle").build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.failed(null);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.d(TAG, "CommunityRemoteDataSource  onResponse: 加载到数据如下: "+jsonData);
                        List<CommunityArticle> itemViewModelList = new ArrayList<>();
                        if (jsonData == null) {
                            callback.failed(null);
                        } else  {
                            itemViewModelList = new Gson().fromJson(jsonData, new TypeToken<List<CommunityArticle>>(){}.getType());
                        }
                        CommunityLocalDataSource.setItemViewModelList(itemViewModelList);
                        callback.succeed(itemViewModelList);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.failed(null);
                    }
                }
            });
    }

    @Override
    public void getCommentList(GeneralCallback callback, int articleId) {

        Request request = new Request.Builder().url("http://imyth.top:8080/community_server/getcommunitycomment?articleId=" + articleId).build();
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            String jsonData = response.body().string();
            Log.d(TAG, "getCommentList: " + jsonData);
            callback.succeed(new Gson().fromJson(jsonData, new TypeToken<List<CommunityComment>>(){}.getType()));
        } catch (NullPointerException | IOException e) {
            callback.failed(e.getMessage());
        }
    }
}
