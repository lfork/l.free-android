package com.lfork.a98620.lfree.data.community.remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.base.network.httpservice.HttpService;
import com.lfork.a98620.lfree.data.community.CommunityDataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.main.community.CommunityArticle;
import com.lfork.a98620.lfree.main.community.CommunityComment;
import com.lfork.a98620.lfree.main.community.CommunityFragmentItemViewModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            List<CommunityArticle> articleList = new Gson().fromJson(response.body().string(), new TypeToken<List<CommunityArticle>>(){}.getType());
            List<CommunityFragmentItemViewModel> itemViewModelList = new ArrayList<>();
            int userId;
            Gson gson = new Gson();
            String responseData;
            RequestBody requestBody;
            for (int i = 0;i < articleList.size();i++) {
                CommunityArticle article = articleList.get(i);
                CommunityFragmentItemViewModel itemViewModel = new CommunityFragmentItemViewModel();
                itemViewModel.setArticle(article.getArticle());
                itemViewModel.setArticleId(article.getArticleId());
                itemViewModel.setArticleTime(article.getArticleTime());
                itemViewModel.setImageUriList(article.getImageUriList());
                itemViewModel.setPublisherId(article.getPublisherId());
                userId = article.getPublisherId();
                requestBody = new FormBody.Builder()
                        .add("studentId", userId+"")
                        .build();
                responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/user_info", requestBody);
                User user = gson.fromJson(responseData, User.class);
                itemViewModel.setHeadImgUri(user.getUserImagePath());
                itemViewModel.setHeadName(user.getUserName());
                itemViewModelList.add(itemViewModel);
            }
            callback.succeed(itemViewModelList);
        } catch (Exception e) {
            callback.failed(e.getMessage());
        }
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