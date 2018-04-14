package com.lfork.a98620.lfree.goodsdetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.common.GlideImageLoader;
import com.lfork.a98620.lfree.databinding.GoodsDetailActBinding;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class GoodsDetailActivity extends AppCompatActivity {

    private GoodsDetailActBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int goodsId = intent.getIntExtra("id", 0);
        int categoryId = intent.getIntExtra("category_id", 0);
        binding = DataBindingUtil.setContentView(this, R.layout.goods_detail_act);
        binding.setViewModel(new GoodsDetailViewModel(this, goodsId, binding));
        initBanner();
        initActionBar("宝贝详情");
    }

    private void initBanner(){
        Banner banner = findViewById(R.id.banner);
        List<String> images = new ArrayList<>();
        images.add("http://www.lfork.top/Test/1.png");
        images.add("http://www.lfork.top/Test/1.png");
        images.add("http://www.lfork.top/Test/1.png");
        images.add("http://www.lfork.top/Test/1.png");
        images.add("http://www.lfork.top/Test/1.png");
        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();

    }

    public void initActionBar(String title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle( title);
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
            case R.id.menu1:
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_action_bar, menu);
        MenuItem item = menu.getItem(0);
        item.setTitle("");
        return true;
    }
}
