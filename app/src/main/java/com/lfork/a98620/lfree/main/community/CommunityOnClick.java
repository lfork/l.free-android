package com.lfork.a98620.lfree.main.community;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;

import com.lfork.a98620.lfree.main.community.articlecontent.ArticleContentActivity;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;

public class CommunityOnClick {
    private static final String TAG = "CommunityOnClick";

    @BindingAdapter({"toUserInfoActivity"})
    public static void toUserInfoActivity(View view, int userId) {
        view.setOnClickListener(view1 -> {
            Log.d(TAG, "toUserInfoActivity: test");
            Intent intent = new Intent(view1.getContext(), UserInfoActivity.class);
            intent.putExtra("user_id", userId);
            view1.getContext().startActivity(intent);
        });

    }
    @BindingAdapter({"toCommentActivity"})
    public static void toCommentActivity(View view, int articleId) {
        Intent intent = new Intent(view.getContext(), ArticleContentActivity.class);
        intent.putExtra("articleId", articleId);
        view.getContext().startActivity(intent);
    }
}
