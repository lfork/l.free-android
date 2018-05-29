package com.lfork.a98620.lfree.main.community.articlecontent;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.community.local.CommunityLocalDataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.local.UserLocalDataSource;
import com.lfork.a98620.lfree.main.community.CommunityArticle;
import com.lfork.a98620.lfree.main.community.CommunityCallback;
import com.lfork.a98620.lfree.main.community.CommunityComment;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ArticleContentActivity extends AppCompatActivity implements CommunityCallback {
    private List<CommunityComment> commentList = null;
    private static final String TAG = "ArticleContentActivity";
    private ViewDataBinding binding;
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
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.article_content);
                for (CommunityComment comment : commentList) {
                    CommentView commentView = new CommentView(ArticleContentActivity.this);
                    commentView.setText(comment.getComment());
                    linearLayout.addView(commentView);
                }
            }
        });
    }

    public void commentTo(View view) {
        EditText editText = (EditText) findViewById(R.id.review_edit);
        String aComment = editText.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommunityComment comment = new CommunityComment();
                comment.setArticleId(article.getArticleId());
                comment.setComment(aComment);
                comment.setCommentTime("2018-01-09-23-11-11");
                List<User> userList = DataSupport.where("isLogin=?", "1").find(User.class);
                comment.setReviewerId(userList.get(0).getId());
                String jsonData = new Gson().toJson(comment);
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder().add("comment", jsonData).build();
                Request request = new Request.Builder()
                                        .url("imyth.top:8080/community_server/getcommunitycomment")
                                        .post(body)
                                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ArticleContentActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.article_content);
                                CommentView commentView = new CommentView(ArticleContentActivity.this);
                                commentView.setText(comment.getComment());
                                linearLayout.addView(commentView);
                            }
                        });
                    }
                });
            }
        }).start();
    }
}
