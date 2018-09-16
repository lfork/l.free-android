package com.lfork.a98620.lfree.searchresult;

import android.app.Activity;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.base.entity.Goods;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 98620 on 2018/5/5.
 */
public class GoodsSearchViewModel extends BaseViewModel {


    public final ObservableField<String> recommendKeyword = new ObservableField<>();


    public ObservableArrayList<GoodsSearchItemViewModel> items = new ObservableArrayList<>();

    private GoodsSearchNavigator navigator;



    GoodsSearchViewModel(Activity context, String recommendKeyword ) {
        super(context);
        this.recommendKeyword.set(recommendKeyword);
        dataIsLoading.set(false);
    }

    public void cancel(){
        if (navigator != null){
            navigator.cancelSearch();
        }
    }


    public void startGoodsSearch(String keyword) {
        dataIsLoading.set(true);
        new Thread(() -> {
            GoodsDataRepository.INSTANCE.goodsSearch(new DataSource.GeneralCallback<List<Goods>>() {
                @Override
                public void succeed(List<Goods> goodsList) {
                    ArrayList<GoodsSearchItemViewModel> tempItems = new ArrayList<>();
                    for (Goods g : goodsList) {
                        GoodsSearchItemViewModel vm = new GoodsSearchItemViewModel(getContext(), g);
                        vm.setNavigator(navigator);
                        tempItems.add(vm);
                    }
                    dataIsLoading.set(false);

                    items.clear();
                    items.addAll(tempItems);

                    if (tempItems.size() > 0) {
                        dataIsEmpty.set(false);
                        showToast("搜索完成");
                    } else{
                        showToast("没有搜索到相关信息");
                    }


                }

                @Override
                public void failed(String log) {
                    dataIsLoading.set(false);
                    showToast(log);

                }
            }, keyword);
        }).start();
    }


    public void setNavigator(GoodsSearchNavigator navigator) {
        super.setNavigator(navigator);
        this.navigator = navigator;
    }

    @Override
    public void start() {
        super.start();//暂时没有需要预加载的任务
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigator = null;
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }
}
