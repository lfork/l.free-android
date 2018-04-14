package com.lfork.a98620.lfree.main.index;

import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.common.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.source.GoodsDataRepository;
import com.lfork.a98620.lfree.databinding.MainIndexViewpagerItemBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by 98620 on 2018/3/31.
 */

public class IndexPagerItemViewModel extends BaseViewModel {
    private static final String TAG = "IndexPagerItemViewModel";

    private Category category;
    //    MainIndexVBinding binding;
    private MainIndexViewpagerItemBinding binding;
    private List<Goods> goodsList;
    private List<GoodsItemViewModel> models = new ArrayList<>();
    private FragmentActivity context;
    private GoodsRecyclerViewItemAdapter adapter;
    private boolean isInitialized = false;


    IndexPagerItemViewModel(Category category, ViewDataBinding binding, FragmentActivity context) {
        super(context);
        this.category = category;
        this.binding = (MainIndexViewpagerItemBinding) binding;
        this.context = context;
        initUI();

    }


    private void initUI(){
        RecyclerView recyclerView = binding.mainIndexRecycle;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GoodsRecyclerViewItemAdapter<>(models, R.layout.goods_recycle_item);
        recyclerView.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshGoodsList();
                binding.swipeRefresh.setRefreshing(false);
            }
        });  //刷新监听
    }

    private void refreshUI(){
        adapter.notifyDataSetChanged();
        dataIsLoading.set(false);
    }

    /**
     * 先加载空白的recyclerView，然后再异步加载数据，最后再刷新recyclerView
     */
    void initData() {
        if (isInitialized){
            return;
        }
        isInitialized = true;
        refreshGoodsList();

    }

    private void loadMoreData(){

    }

    private void refreshGoodsList() {
        GoodsDataRepository repository = GoodsDataRepository.getInstance();
        new Thread(() -> {

            //强行等一秒，这样可以优化加载效果。缓冲一下
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String cursor ;

            if (goodsList != null && goodsList.size() > 0){
                cursor = goodsList.get(goodsList.size() - 1).getPublishDate();
            } else {
                cursor =   DateFormat.format("yyyy-MM-dd HH:mm:ss",new Date()).toString();
            }

            Log.d(TAG, "initData: ");

            repository.getGoodsList(new DataSource.GeneralCallback<List<Goods>>() {
                @Override
                public void success(List<Goods> data) {
                    goodsList = data;
                    models.clear();
                    for (Goods g : goodsList) {
                        models.add(new GoodsItemViewModel(context, g, category.getId()));
                    }
                    context.runOnUiThread(() -> {
                        refreshUI();
                    });
                }
                @Override
                public void failed(String log) {
                    context.runOnUiThread(() -> {
                        refreshUI();
                        Toast.makeText(context, log, Toast.LENGTH_LONG).show();
                    });
                }
            }, cursor, category.getId());
        }).start();
    }

}
