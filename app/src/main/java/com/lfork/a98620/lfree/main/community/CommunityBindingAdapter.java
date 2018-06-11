package com.lfork.a98620.lfree.main.community;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.view.View;

import com.lfork.a98620.lfree.articlecontent.ArticleContentActivity;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;

public class CommunityBindingAdapter {

    @BindingAdapter("android:toUserInfoActivity")
    public static void toUserInfoActivity(View view, int userId) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserInfoActivity.class);
                intent.putExtra("user_id", userId);
                view.getContext().startActivity(intent);
            }
        });
    }

    @BindingAdapter("android:toArticleContentActivity")
    public static void toArticleContentActivity(View view, int articleId) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ArticleContentActivity.class);
                intent.putExtra("articleId", articleId);
                view.getContext().startActivity(intent);
            }
        });
    }
}
