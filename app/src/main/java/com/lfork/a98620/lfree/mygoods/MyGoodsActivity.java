package com.lfork.a98620.lfree.mygoods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.BaseActivity;
import com.lfork.a98620.lfree.databinding.MyGoodsActBinding;
import com.lfork.a98620.lfree.base.adapter.RecyclerViewItemAdapter;
import com.lfork.a98620.lfree.goodsdetail.GoodsDetailActivity;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.Objects;

public class MyGoodsActivity extends BaseActivity implements MyGoodsActivityNavigator , MyGoodsItemNavigator{

    private MyGoodsActBinding binding;

    private MyGoodsViewModel viewModel;

    RecyclerViewItemAdapter<MyGoodsItemViewModel> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
        adapter.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.my_goods_act);
        viewModel = new MyGoodsViewModel(this);
        binding.setViewModel(viewModel);
        viewModel.setNavigator(this);
        setupRecyclerView();
        initActionBar();
    }

    private void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayShowTitleEnabled(true);
        actionBar.setTitle("我的商品");
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


    private void setupRecyclerView(){
        RecyclerView recyclerView = binding.myGoodsRecycle;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewItemAdapter<>(R.layout.my_goods_recycle_item);
        adapter.setNavigator(this);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void showToast(String msg) {
        runOnUiThread(() -> ToastUtil.showShort(getApplicationContext(), msg));
    }

    @Override
    public void refreshFinished(String log) {

    }

    @Override
    public void loadMoreFinished(String log) {

    }

    @Override
    public void openGoodsDetail(int goodsId, int categoryId) {
        GoodsDetailActivity.startActivity(this, goodsId, categoryId);
    }
}
