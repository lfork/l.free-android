package com.lfork.a98620.lfree.main.community.articlecontent;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.community.local.CommunityLocalDataSource;
import com.lfork.a98620.lfree.main.community.CommunityCallback;
import com.lfork.a98620.lfree.main.community.CommunityComment;
import com.lfork.a98620.lfree.main.community.CommunityFragmentItemViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class ArticleContentActivity extends AppCompatActivity implements CommunityCallback {
    private List<CommunityComment> commentList = null;
    private static final String TAG = "ArticleContentActivity";
    private ViewDataBinding binding;
    private ArticleContentActivityViewModel viewModel;
    private CommunityFragmentItemViewModel article = null;

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
                    commentView.setText("      " + comment.getComment());
                    linearLayout.addView(commentView);
                }
            }
        });
    }
}
