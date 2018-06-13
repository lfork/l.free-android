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
    public void getArticleList(String fromTime, GeneralCallback callback) {
        String url = "http://imyth.top:8080/community_server/getcommunityarticle?fromTime=" + fromTime;
        Log.d(TAG, "getArticleList: 请求url"+url);
        Request request = new Request.Builder().url(url).build();
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
                        List<CommunityArticle> articleList = new ArrayList<>();
                        if (jsonData == null) {
                            callback.failed(null);
                        } else  {
                            articleList = new Gson().fromJson(jsonData, new TypeToken<List<CommunityArticle>>(){}.getType());
                        }
                        CommunityLocalDataSource.setArticleList(articleList);
                        callback.succeed(articleList.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.failed(null);
                    }
                }
            });
    }

    @Override
    public void getCommentList(GeneralCallback callback, int articleId) {

        String url = "http://imyth.top:8080/community_server/getcommunitycomment?articleId=" + articleId;
        Log.d(TAG, "getCommentList: url = " + url);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            String jsonData = response.body().string();
            Log.d(TAG, "getCommentList: " + jsonData);
            List<CommunityComment> commentList = new Gson().fromJson(jsonData, new TypeToken<List<CommunityComment>>(){}.getType());
            callback.succeed(commentList);
        } catch (NullPointerException | IOException e) {
            callback.failed(e.getMessage());
        }
    }
}