package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.databinding.MainIndexViewpagerItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 98620 on 2018/6/4.
 */
public class PagerItemView extends View  implements DataRefreshListener, SwipeRefreshLayout.OnRefreshListener{

    private List<GoodsItemViewModel> models = new ArrayList<>();

    private ViewGroup parent;

    private MainIndexViewpagerItemBinding binding;

    public PagerItemView(Context context) {
        super(context);
    }


    public PagerItemView(ViewGroup parent) {
        this(parent.getContext());
        onCreate();
    }



    public void onCreate(){
        binding = DataBindingUtil.inflate(LayoutInflater
                .from(parent.getContext()), R.layout.main_index_viewpager_item, parent, false);
        IndexPagerItemViewModel viewModel = new IndexPagerItemViewModel(parent.getContext());
        binding.setViewModel(viewModel);
        setupRecyclerView();

    }

    public void onResume(){

    }

    public void onDestroy(){
        parent = null;
    }



    public View getView(){
        if (binding != null) {
            return binding.getRoot();
        }

        return null;
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = binding.mainIndexRecycle;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        GoodsRecyclerViewItemAdapter adapter = new GoodsRecyclerViewItemAdapter<>(models, R.layout.goods_recycle_item);
        recyclerView.setAdapter(adapter);
        adapter.setListener(this);
        binding.swipeRefresh.setOnRefreshListener(this); //刷新监听
    }

    @Override
    public void startRefreshing() {

    }

    @Override
    public void endRefresh() {

    }

    /**
     *  SwipeRefreshLayout的OnRefresh 这里就需要调用viewModel的一些操作进行数据刷新了
     */
    @Override
    public void onRefresh() {

    }
}
