package com.lfork.a98620.lfree.main.index;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.lfork.a98620.lfree.BaseViewModel;
import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.entity.Goods;
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

    private String category;
    //    MainIndexVBinding binding;
    private MainIndexViewpagerItemBinding binding;
    private List<Goods> goodsList;
    private List<GoodsItemViewModel> models = new ArrayList<>();
    private Context context;


    IndexPagerItemViewModel(String category, ViewDataBinding binding, Context context) {
        super();
        this.category = category;
        this.binding = (MainIndexViewpagerItemBinding) binding;
        this.context = context;
        initGoodsList();

    }
    //需要recyclerView  需要parentView 需要goodsList 需要categoryName


    /**
     * 先加载空白的recyclerView，然后再异步加载数据，最后再刷新recyclerView
     */
    private void initGoodsList() {
        goodsList = new ArrayList<>();
        for (int i =0; i < 20; i++){
            Goods goods = new Goods();
            goods.setDescription("aaaaaaa");
            goods.setName("goods" + i);
            goods.setId(i);
            goods.setPrice(String.valueOf(i * 100));
            goods.setImagePath("???");
            goodsList.add(goods);
            Log.d(TAG, "initGoodsList: test1" + i );
        }
        Log.d(TAG, "initGoodsList: test1" );

        for (Goods g : goodsList) {
            models.add(new GoodsItemViewModel(g));
        }


        RecyclerView recyclerView = binding.mainIndexRecycle;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //        recyclerView .setLayoutManager(new LinearLayoutManager(context,
//                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerViewItemAdapter<>(models, R.layout.goods_recycle_item));
    }

    private void refreshGoodsList() {

    }

}
