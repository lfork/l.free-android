package com.lfork.a98620.lfree.publisharticle;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.base.entity.User;
import com.lfork.a98620.lfree.databinding.ActivityPublishArticleBinding;
import com.lfork.a98620.lfree.main.community.CommunityArticle;

import org.litepal.crud.DataSupport;

import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishArticleActivity extends AppCompatActivity {
    private CommunityArticle article = new CommunityArticle();
    private static final String TAG = "PublishArticleActivity";
    private ActivityPublishArticleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_publish_article);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_action_bar, menu);
        MenuItem item = menu.getItem(0);
        item.setTitle("发布");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                String articleTime = DateFormat.format("yyyy-MM-dd-HH-mm-ss", new Date()).toString();
                User user = DataSupport.where("isLogin=?", "1").find(User.class).get(0);
                article.setArticleTime(articleTime);
                article.setPublisherId(user.getUserId());
                Log.d(TAG, "onOptionsItemSelected: " + article.getArticle() + "  " + articleTime + "  "+article.getPublisherId());
                binding.setViewModel(article);
                article.setArticle(binding.articleEdit.getText().toString().trim());
                if (!article.getArticle().equals("")) {
                    new Thread(() -> {
                        try {
                            Gson gson = new Gson();
                            Log.d(TAG, "onOptionsItemSelected: " +
                                    article.getArticle() + article.getPublisherId() +
                                    article.getArticleTime());
                            String jsonData = gson.toJson(article);
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("article", jsonData)
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://imyth.top:8080/community_server/publisharticle")
                                    .post(requestBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            if (responseData.equals("1")) {     //发布成功
                                runOnUiThread(() -> {
                                    Log.d(TAG, "run: 成功");
                                    Toast.makeText(PublishArticleActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                });
                            } else {
                                runOnUiThread(() -> {
                                    Log.d(TAG, "run: 失败");
                                    Toast.makeText(PublishArticleActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "不能发布空白", Toast.LENGTH_SHORT).show();
                    });
                }
                break;
             default:
                    break;
        }
        return true;
    }
}
