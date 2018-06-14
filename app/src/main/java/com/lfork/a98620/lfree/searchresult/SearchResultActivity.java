package com.lfork.a98620.lfree.searchresult;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.base.viewmodel.GoodsItemViewModel;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.databinding.SearchResultActBinding;
import com.lfork.a98620.lfree.util.ToastUtil;
import com.lfork.a98620.lfree.util.adapter.RecyclerViewItemAdapter;

public class SearchResultActivity extends AppCompatActivity implements ViewModelNavigator {

    private SearchResultActBinding binding;

    private SearchResultViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.search_result_act);
        Intent intent = getIntent();
        String recommendKeyword = intent.getStringExtra("recommend_keyword");
        viewModel = new SearchResultViewModel(this, recommendKeyword);
        binding.setViewModel(viewModel);
        viewModel.setNavigator(this);
        setupRecyclerView();

        binding.toolbarSearch.setOnSearchClickListener(v -> {
            ToastUtil.showLong(this, "SearchTest");
        });

        binding.toolbarSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ToastUtil.showLong(getApplicationContext(), query);
                viewModel.startGoodsSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ToastUtil.showLong(getApplicationContext(), newText);
                return false;
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.searchRecycler;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewItemAdapter<GoodsItemViewModel> adapter = new RecyclerViewItemAdapter<>(viewModel.items, R.layout.my_goods_recycle_item);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void showMessage(String msg) {
        runOnUiThread(() -> {
            if (msg.equals("搜索完成")) {
                binding.searchRecycler.getAdapter().notifyDataSetChanged();
            }

            ToastUtil.showShort(getBaseContext(), msg);
        });

    }
}
