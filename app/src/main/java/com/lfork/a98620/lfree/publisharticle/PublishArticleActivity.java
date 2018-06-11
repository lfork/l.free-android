package com.lfork.a98620.lfree.publisharticle;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.databinding.ActivityPublishArticleBinding;
import com.lfork.a98620.lfree.main.community.CommunityArticle;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishArticleActivity extends AppCompatActivity {
    private CommunityArticle article = new CommunityArticle();
    private static final String TAG = "PublishArticleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPublishArticleBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_publish_article);
        Calendar calendar = Calendar.getInstance();
        String articleTime = calendar.get(Calendar.YEAR)
                + "-" + calendar.get(Calendar.MONTH)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                + "-" + calendar.get(Calendar.HOUR_OF_DAY)
                + "-" + calendar.get(Calendar.MINUTE)
                + "-" + calendar.get(Calendar.SECOND);
        User user = DataSupport.where("isLogin=?", "1").find(User.class).get(0);
        article.setArticleTime(articleTime);
        article.setPublisherId(user.getUserId());
        article.setHeadImgUri(user.getUserImagePath());
        article.setHeadName(user.getUserName());
        binding.setViewModel(article);
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
                if (!article.getArticle().trim().equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
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
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d(TAG, "run: 成功");
                                            Toast.makeText(PublishArticleActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent();
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d(TAG, "run: 失败");
                                            Toast.makeText(PublishArticleActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "不能发布空白", Toast.LENGTH_SHORT).show();
                }
                break;
             default:
                    break;
        }
        return true;
    }
}
