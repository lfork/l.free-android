package com.lfork.a98620.lfree.searchresult;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.adapter.RecyclerViewItemAdapter;
import com.lfork.a98620.lfree.databinding.GoodsSearchActBinding;
import com.lfork.a98620.lfree.goodsdetail.GoodsDetailActivity;
import com.lfork.a98620.lfree.util.ToastUtil;

public class GoodsSearchActivity extends AppCompatActivity implements GoodsSearchNavigator {

    private GoodsSearchActBinding binding;

    private GoodsSearchViewModel viewModel;

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.goods_search_act);
        Intent intent = getIntent();
        String recommendKeyword = intent.getStringExtra("recommend_keyword");
        viewModel = new GoodsSearchViewModel(this, recommendKeyword);
        binding.setViewModel(viewModel);
        viewModel.setNavigator(this);
        setupRecyclerView();

        binding.toolbarSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ToastUtil.showLong(getApplicationContext(), query);
                viewModel.startGoodsSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                ToastUtil.showLong(getApplicationContext(), newText);
                return false;
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.searchRecycler;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewItemAdapter<GoodsSearchItemViewModel> adapter = new RecyclerViewItemAdapter<>( R.layout.my_goods_recycle_item);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void showToast(String msg) {
        runOnUiThread(() -> {
            ToastUtil.showShort(getBaseContext(), msg);
        });

    }

    @Override
    public void openGoodsDetail(int goodsId,int categoryId) {
        GoodsDetailActivity.startActivity(this, goodsId, categoryId);
    }

    @Override
    public void cancelSearch() {
        finish();
    }
}
