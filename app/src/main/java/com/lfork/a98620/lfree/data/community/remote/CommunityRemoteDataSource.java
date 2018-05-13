package com.lfork.a98620.lfree.data.community.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.base.network.httpservice.HttpService;
import com.lfork.a98620.lfree.data.community.CommunityDataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.main.community.CommunityArticle;
import com.lfork.a98620.lfree.main.community.CommunityComment;
import com.lfork.a98620.lfree.main.community.CommunityFragmentItemViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.yalantis.ucrop.UCropFragment.TAG;

public class CommunityRemoteDataSource implements CommunityDataSource {

    private static CommunityRemoteDataSource INSTANCE;

    public static CommunityRemoteDataSource getINSTANCE() {
        if (INSTANCE == null) {
            return new CommunityRemoteDataSource();
        } else {
            return INSTANCE;
        }
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
                        List<CommunityArticle> articleList = new ArrayList<>();
                        if (jsonData == null) {
                            callback.failed(null);
                        } else  {
                            articleList = new Gson().fromJson(jsonData, new TypeToken<List<CommunityArticle>>(){}.getType());
                        }
                        List<CommunityFragmentItemViewModel> itemViewModelList = new ArrayList<>();
                        for (int i = 0;i < articleList.size();i++) {
                            CommunityArticle article = articleList.get(i);
                            CommunityFragmentItemViewModel itemViewModel = new CommunityFragmentItemViewModel();
                            itemViewModel.setArticle(article.getArticle());
                            itemViewModel.setArticleId(article.getArticleId());
                            itemViewModel.setArticleTime(article.getArticleTime());
                            itemViewModel.setImageUriListFromStringList(article.getImageUriList());
                            itemViewModel.setPublisherId(article.getPublisherId());
                            itemViewModel.setHeadImgUri(article.getHeadImgUri());
                            itemViewModel.setHeadName(article.getHeadName());
                            itemViewModelList.add(itemViewModel);
                            Log.d(TAG, "onResponse :getArticle " + itemViewModel.getArticle());
                            Log.d(TAG, "onResponse:getArticleId " + itemViewModel.getArticleId());
                            Log.d(TAG, "onResponse:getArticleTime " + itemViewModel.getArticleTime());
                            Log.d(TAG, "onResponse:getHeadImgUri " + itemViewModel.getHeadImgUri());
                            Log.d(TAG, "onResponse:getHeadName " + itemViewModel.getHeadName());
                            Log.d(TAG, "onResponse:getPublisherId " + itemViewModel.getPublisherId());
                            Log.d(TAG, "onResponse:getImageUriList " + itemViewModel.getImageUriList());
                        }
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
            callback.succeed(new Gson().fromJson(response.body().string(), new TypeToken<List<CommunityComment>>(){}.getType()));
        } catch (Exception e) {
            callback.failed(e.getMessage());
        }
    }
}
