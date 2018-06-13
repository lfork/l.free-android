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
import com.lfork.a98620.lfree.util.adapter.RecyclerViewItemAdapter;

public class MyGoodsActivity extends BaseActivity implements MyGoodsActivityNavigator {

    private MyGoodsActBinding binding;

    private MyGoodsViewModel viewModel;

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.start();
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
        actionBar.setDisplayShowTitleEnabled(true);
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
        RecyclerViewItemAdapter<MyGoodsItemViewModel> adapter = new RecyclerViewItemAdapter<>(viewModel.items, R.layout.my_goods_recycle_item);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.destroy();
    }

    @Override
    public void notifyDataChanged() {
        binding.myGoodsRecycle.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showMessage(String msg) {

    }
}
