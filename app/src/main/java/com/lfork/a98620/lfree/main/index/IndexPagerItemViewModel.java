package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.lfork.a98620.lfree.BaseViewModel;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Category;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.source.GoodsDataRepository;
import com.lfork.a98620.lfree.databinding.MainIndexViewpagerItemBinding;
import com.lfork.a98620.lfree.main.RecyclerViewItemAdapter;

import java.util.ArrayList;
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
    private RecyclerViewItemAdapter adapter;
    private boolean isInitialized = false;
    public final ObservableBoolean dataIsLoading = new ObservableBoolean(true);


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
        adapter = new RecyclerViewItemAdapter<>(models, R.layout.goods_recycle_item);
        recyclerView.setAdapter(adapter);
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
        GoodsDataRepository repository = GoodsDataRepository.getInstance();
        new Thread(() -> {

            //强行等一秒，这样可以优化加载效果。缓冲一下
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            repository.getGoodsList(new DataSource.GeneralCallback<List<Goods>>() {
                @Override
                public void success(List<Goods> data) {
                    goodsList = data;
                    for (Goods g : goodsList) {
                        models.add(new GoodsItemViewModel(g));
                    }

                    context.runOnUiThread(() -> {
                        refreshUI();
                    });
                }
                @Override
                public void failed(String log) {

                }
            }, 1, category.getId());
        }).start();



//        goodsList = new ArrayList<>();
//        for (int i =0; i < 20; i++){
//            Goods goods = new Goods();
//            goods.setDescription("aaaaaaa");
//            goods.setName("goods" + i);
//            goods.setId(i);
//            goods.setPrice(String.valueOf(i * 100));
//            goods.setImagePath("???");
//            goodsList.add(goods);
////            Log.d(TAG, "initGoodsList: test1" + i );
//        }
////        Log.d(TAG, "initGoodsList: test1" );
//




    }

    private void refreshGoodsList() {

    }

}
