package com.lfork.a98620.lfree.main.community;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.view.View;

import com.lfork.a98620.lfree.articlecontent.ArticleContentActivity;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;

public class CommunityBindingAdapter {

    @BindingAdapter("android:toUserInfoActivity")
    public static void toUserInfoActivity(View view, int userId) {
        view.setOnClickListener((view1) -> {
            Intent intent = new Intent(view1.getContext(), UserInfoActivity.class);
            intent.putExtra("user_id", userId);
            view1.getContext().startActivity(intent);
        });
    }

    @BindingAdapter("android:toArticleContentActivity")
    public static void toArticleContentActivity(View view, int articleId) {
        view.setOnClickListener((view1) -> {
            Intent intent = new Intent(view1.getContext(), ArticleContentActivity.class);
            intent.putExtra("articleId", articleId);
            view1.getContext().startActivity(intent);
        });
    }
}
