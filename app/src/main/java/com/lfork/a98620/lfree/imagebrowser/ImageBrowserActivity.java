package com.lfork.a98620.lfree.imagebrowser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.chrisbanes.photoview.PhotoView;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.bindingadapter.ImageBinding;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 图片浏览器
 */
public class ImageBrowserActivity extends AppCompatActivity {


    private ArrayList<String>  urls;
    private int position;

    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_browser_act);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        Bundle bundle = intent.getBundleExtra("bundle");
        urls = bundle.getStringArrayList("urls");
        photoView = findViewById(R.id.big_image);

        if (urls != null && urls.size() > 0) {
            ImageBinding.setImageNoCache(photoView, urls.get(position));
        }

        setupActionBar();
    }


    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(true);
        actionBar.setTitle("原图查看");
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public static void actionStart(Context context, ArrayList<String>  urls, int position){
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("urls", urls);
        intent.putExtra("bundle", bundle);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
}
