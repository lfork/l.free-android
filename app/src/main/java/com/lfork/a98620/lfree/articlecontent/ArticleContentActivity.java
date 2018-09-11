package com.lfork.a98620.lfree.articlecontent;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.community.local.CommunityLocalDataSource;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.databinding.MainCommunityArticleContentActivityBinding;
import com.lfork.a98620.lfree.main.community.CommunityArticle;
import com.lfork.a98620.lfree.main.community.CommunityCallback;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
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
    private boolean fromCommentButton = false;//是否是点击评论按钮跳转过来
    private String shareUrl = "http://imyth.top:8080/community_server/getcommunitycomment?articleId=";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("动态内容");
            actionBar.setLogo(R.drawable.login_logo);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.main_community_article_content_activity);
        binding.executePendingBindings();
        Intent intent = getIntent();
        Log.d(TAG, "onCreateView: 读取本地数据");
        int articleId = intent.getIntExtra("articleId", -1);
        shareUrl += articleId;
        fromCommentButton = intent.getBooleanExtra("fromCommentButton", false);
        article = CommunityLocalDataSource.getLocalArticle(articleId);
        viewModel = new ArticleContentActivityViewModel(this, article != null ? article.getArticleId():-1);
        binding.setVariable(BR.viewModel, article);

        viewModel.loadData(ArticleContentActivity.this, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.common_action_bar, menu);
        menu.getItem(0).setTitle("分享");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu1:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND)
                        .setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareUrl);
                startActivity(Intent.createChooser(intent, "分享到"));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void callback(Object data, int type) {
        if (data != null) {
            runOnUiThread(() -> {
                commentList = new ArrayList<>();
                commentList.addAll((List<CommunityComment>) data);
                if (commentList.size() != 0) {
                    binding.noComment.setVisibility(View.GONE);
                }
                recyclerView = binding.articleComment;
                adapter = new CommunityCommentAdapter(commentList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ArticleContentActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                recyclerView.setNestedScrollingEnabled(false);
                if (fromCommentButton) {
                    binding.scrollView.scrollTo(0, binding.linearLayout.getMeasuredHeight() - binding.scrollView.getHeight());
                } else {
                    binding.scrollView.scrollTo(0, -1);
                }
            });
        }
    }

    public void commentTo(View view) {
        EditText editText = binding.reviewEdit;
        String aComment = editText.getText().toString().trim();
        editText.setText("");
        if (!aComment.equals("")) {
            new Thread(() -> {
                try {
                    CommunityComment comment = new CommunityComment();
                    String articleTime = DateFormat.format("yyyy-MM-dd-HH-mm-ss", new Date()).toString();
                    comment.setArticleId(article.getArticleId());
                    comment.setComment(aComment);
                    comment.setCommentTime(articleTime);
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
                        runOnUiThread(() -> {
                            commentList.add(comment);
                            adapter.notifyDataSetChanged();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
