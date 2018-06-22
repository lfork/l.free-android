package com.lfork.a98620.lfree.main.community;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.view.View;

import com.lfork.a98620.lfree.articlecontent.ArticleContentActivity;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;
import com.lfork.a98620.lfree.util.ShareUtil;

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
            intent.putExtra("fromCommentButton", false);
            view1.getContext().startActivity(intent);
        });
    }

    @BindingAdapter("android:shareTo")
    public static void shareTo(View view, String url) {
        view.setOnClickListener((view1) -> {
            ShareUtil.shareTextBySystem(view.getContext(), url, "分享到");
        });
    }

    @BindingAdapter("android:toArticleComment")
    public static void toArticleComment(View view, int articleId) {
        view.setOnClickListener((view1) -> {
            Intent intent = new Intent(view.getContext(), ArticleContentActivity.class);
            intent.putExtra("articleId", articleId);
            intent.putExtra("fromCommentButton", true);
            view1.getContext().startActivity(intent);
        });
    }
}
