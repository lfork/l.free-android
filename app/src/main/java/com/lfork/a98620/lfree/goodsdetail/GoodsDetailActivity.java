package com.lfork.a98620.lfree.goodsdetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.image.GlideImageLoader;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.databinding.GoodsDetailActBinding;
import com.lfork.a98620.lfree.util.adapter.RecyclerViewItemAdapter;
import com.youth.banner.Banner;

public class GoodsDetailActivity extends AppCompatActivity implements ViewModelNavigator {

    private GoodsDetailActBinding binding;

    private GoodsDetailViewModel viewModel;

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int goodsId = intent.getIntExtra("id", 0);
        int categoryId = intent.getIntExtra("category_id", 0);
        binding = DataBindingUtil.setContentView(this, R.layout.goods_detail_act);
        viewModel = new GoodsDetailViewModel(this, goodsId, binding);
        viewModel.setNavigator(this);
        binding.setViewModel(viewModel);
        setupRecyclerView();
        initActionBar("宝贝详情");
        initBanner();
    }

    private void initBanner(){
        Banner banner = findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader()).start();
    }


    public void initActionBar(String title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle( title);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);

    }

    private void setupRecyclerView(){
        RecyclerView recyclerView =   binding.reviewContent.reviewRecycle;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewItemAdapter<String> adapter = new RecyclerViewItemAdapter<>(viewModel.reviewItems, R.layout.goods_detail_comment_contacts_item);
        recyclerView.setAdapter(adapter);
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

    @Override
    public void notifyDataChanged() {
        binding.reviewContent.reviewRecycle.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void setParam1(String param) {

    }

    @Override
    public void setParam2(String param) {

    }
}
