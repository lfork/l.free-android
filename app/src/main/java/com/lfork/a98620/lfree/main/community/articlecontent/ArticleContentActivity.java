package com.lfork.a98620.lfree.main.community.articlecontent;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lfork.a98620.lfree.R;

public class ArticleContentActivity extends AppCompatActivity {
    private ViewDataBinding binding;
    private ArticleContentActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_community_article_content_activity);
        viewModel = new ArticleContentActivityViewModel(this);
        Intent intent = getIntent();
        int publisherId = (int) intent.getIntExtra("publisherId", 0);
        TextView textView = (TextView) findViewById(R.id.test);
        textView.setText("动态来自" + publisherId);
    }
}
