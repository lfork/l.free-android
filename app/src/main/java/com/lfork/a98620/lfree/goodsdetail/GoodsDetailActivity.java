package com.lfork.a98620.lfree.goodsdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lfork.a98620.lfree.imagebrowser.ImageBrowserActivity;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.image.GlideImageLoader;
import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.databinding.GoodsDetailActBinding;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;
import com.lfork.a98620.lfree.util.ToastUtil;
import com.lfork.a98620.lfree.util.adapter.RecyclerViewItemAdapter;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Objects;

public class GoodsDetailActivity extends AppCompatActivity implements GoodsDetailNavigator {

    private GoodsDetailActBinding binding;

    private GoodsDetailViewModel viewModel;

    @Override
    protected void onStart() {
        super.onStart();
        if (viewModel != null) {
            viewModel.setNavigator(this);
            viewModel.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int goodsId = intent.getIntExtra("id", 0);
       // int categoryId = intent.getIntExtra("category_id", 0);
        binding = DataBindingUtil.setContentView(this, R.layout.goods_detail_act);
        viewModel = new GoodsDetailViewModel(this, goodsId);

        binding.setViewModel(viewModel);
        setupRecyclerView();
        initActionBar("宝贝详情");
        initBanner();
    }

    private void initBanner(){
        Banner banner = findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                viewModel.onClickImage(position);
            }
        });
    }


    public void initActionBar(String title){
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);

    }

    private void setupRecyclerView(){
        RecyclerView recyclerView =   binding.reviewContent.reviewRecycle;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewItemAdapter<ReviewItemViewModel> adapter = new RecyclerViewItemAdapter<>(viewModel.reviewItems, R.layout.goods_detail_comment_contacts_item);
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
    public void notifyReviewChanged() {
        runOnUiThread(() -> {
            binding.reviewContent.reviewRecycle.getAdapter().notifyDataSetChanged();
            binding.reviewContent.reviewRecycle.scrollToPosition(viewModel.reviewItems.size() - 1);//将recyclerView定位到最后一行
        });
    }

    /**
     *  关闭软键盘
     */
    @Override
    public void closeSoftKeyBoard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void openUserInfo(int userId) {
        UserInfoActivity.activityStart(this, userId);
    }

    @Override
    public void openChatWindow(String username, int userId) {
        ChatWindowActivity.activityStart(this, username, userId);
    }

    @Override
    public void openBigImages(ObservableArrayList<String> images, int position) {
        ImageBrowserActivity.actionStart(this, images, position);
    }

    @Override
    public void refreshBanner(ArrayList<String> images) {
        runOnUiThread(() -> {
            binding.banner.update(images);      //刷新轮播图
        });
    }

    @Override
    public void showMessage(String msg) {
        runOnUiThread(() -> ToastUtil.showShort(getBaseContext(), msg));
    }
}
