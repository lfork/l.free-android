package com.lfork.a98620.lfree.main.community.articlecontent;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.community.local.CommunityLocalDataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.databinding.MainCommunityArticleContentActivityBinding;
import com.lfork.a98620.lfree.main.community.CommunityArticle;
import com.lfork.a98620.lfree.main.community.CommunityCallback;

import org.litepal.crud.DataSupport;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ArticleContentActivity extends AppCompatActivity implements CommunityCallback {
    private List<CommunityComment> commentList = null;
    private RecyclerView recyclerView;
    private CommunityCommentAdapter adapter;
    private static final String TAG = "ArticleContentActivity";
    private MainCommunityArticleContentActivityBinding binding;
    private ArticleContentActivityViewModel viewModel;
    private CommunityArticle article = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_community_article_content_activity);
        binding.executePendingBindings();
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: 读取本地数据");
        int articleId = intent.getIntExtra("articleId", -1);
        article = CommunityLocalDataSource.getLocalArticle(articleId);
        viewModel = new ArticleContentActivityViewModel(this, article != null ? article.getArticleId():-1);
        binding.setVariable(BR.viewModel, article);
        viewModel.loadData(ArticleContentActivity.this, false);

    }

    @Override
    public void callback(Object data, int type) {
        commentList = (List<CommunityComment>) data;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView = binding.articleComment;
                adapter = new CommunityCommentAdapter(commentList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ArticleContentActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                recyclerView.setNestedScrollingEnabled(false);
            }
        });
    }

    public void commentTo(View view) {
        EditText editText = binding.reviewEdit;
        String aComment = editText.getText().toString().trim();
        editText.setText("");
        if (!aComment.equals("")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CommunityComment comment = new CommunityComment();
                        comment.setArticleId(article.getArticleId());
                        comment.setComment(aComment);
                        comment.setCommentTime("2018-01-09-23-11-11");
                        List<User> userList = DataSupport.where("isLogin=?", "1").find(User.class);
                        comment.setReviewerId(userList.get(0).getId());
                        comment.setReviewer(userList.get(0).getUserName());
                        String jsonData = new Gson().toJson(comment);
                        Log.d(TAG, "run: 要发出的" + jsonData);
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder().add("comment", jsonData).build();
                        Request request = new Request.Builder()
                                .url("http://imyth.top:8080/community_server/publishcomment")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        String returnData = response.body().string();
                        Log.d(TAG, "run: 返回结果" + returnData);
                        if (!returnData.equals("0")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    commentList.add(comment);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
