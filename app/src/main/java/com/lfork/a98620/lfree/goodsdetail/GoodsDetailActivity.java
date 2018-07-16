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

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.image.GlideImageLoader;
import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.databinding.GoodsDetailActBinding;
import com.lfork.a98620.lfree.goodsuploadupdate.GoodsUploadUpdateActivity;
import com.lfork.a98620.lfree.imagebrowser.ImageBrowserActivity;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;
import com.lfork.a98620.lfree.util.ShareUtil;
import com.lfork.a98620.lfree.util.ToastUtil;
import com.lfork.a98620.lfree.base.adapter.RecyclerViewItemAdapter;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Objects;

public class GoodsDetailActivity extends AppCompatActivity implements GoodsDetailNavigator {

    private GoodsDetailActBinding binding;

    private GoodsDetailViewModel viewModel;

    private MenuItem delete, edit;

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
        int goodsId = intent.getIntExtra("goods_id", 0);
        int categoryId = intent.getIntExtra("category_id", 0);
        binding = DataBindingUtil.setContentView(this, R.layout.goods_detail_act);
        viewModel = new GoodsDetailViewModel(this, goodsId, categoryId);

        binding.setViewModel(viewModel);
        setupRecyclerView();
        initActionBar("宝贝详情");
        initBanner();
    }

    private void initBanner() {
        Banner banner = findViewById(R.id.banner);
        banner.setImageLoader(new GlideImageLoader()).start();
        banner.setOnBannerListener(position -> viewModel.onClickImage(position));
    }


    public void initActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
        // 决定左上角图标的右侧是否有向左的小箭头, true
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 有小箭头，并且图标可以点击
        actionBar.setDisplayShowHomeEnabled(false);

    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.reviewContent.reviewRecycle;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewItemAdapter<ReviewItemViewModel> adapter = new RecyclerViewItemAdapter<>(R.layout.goods_detail_comment_item);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_edit:
                viewModel.updateGoods();
                break;
            case R.id.menu_delete:
                viewModel.deleteGoods();
                break;
            case R.id.menu_share:
                viewModel.shareGoods();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.goods_detail_action_bar, menu);
        edit = menu.getItem(1);
        delete = menu.getItem(2);
        return true;
    }



    @Override
    public void notifyReviewChanged() {
        runOnUiThread(() -> {
            binding.reviewContent.reviewRecycle.scrollToPosition(viewModel.reviewItems.size() - 1);//将recyclerView定位到最后一行
        });
    }

    /**
     * 关闭软键盘
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
    public void setActionBar() {
        runOnUiThread(() -> {
            edit.setVisible(true);
            delete.setVisible(true);
        });

    }

    @Override
    public void shareGoods(String str) {
        runOnUiThread(() -> {
            ShareUtil.shareTextBySystem(getBaseContext(), str, "分享到");
        });
    }

    @Override
    public void deleteGoods(boolean succeed) {
        runOnUiThread(() -> {
            ToastUtil.showShort(getBaseContext(), succeed ?"假装删除成功:后台功能还在开发中":"删除失败");

            if (succeed) {
                finish();
            }
        });
    }

    @Override
    public void deleteReview(boolean succeed, int reviewId) {
        if (reviewId == -1) {
            runOnUiThread(() -> ToastUtil.showShort(getBaseContext(), "假装删除成功:后台功能还在开发中"));
        } else {
            viewModel.deleteReview(reviewId);
        }


    }

    @Override
    public void updateGoods(int goodsId) {
        runOnUiThread(() -> {
            GoodsUploadUpdateActivity.openUpdateActivityForResult(GoodsDetailActivity.this, goodsId, viewModel.getCategoryId());
        });
    }

    @Override
    public void showToast(String msg) {
        runOnUiThread(() -> ToastUtil.showShort(getBaseContext(), msg));
    }

    public static void startActivity(Context context, int goodsId, int categoryId){
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra("goods_id", goodsId);
        intent.putExtra("category_id", categoryId);
        context.startActivity(intent);
    }


}
