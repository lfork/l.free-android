package com.lfork.a98620.lfree.searchresult;

import android.app.Activity;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.base.viewmodel.GoodsItemViewModel;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 98620 on 2018/5/5.
 */
public class SearchResultViewModel extends BaseViewModel {

    private Activity context;


    public final ObservableField<String> recommendKeyword = new ObservableField<String>();


    public ObservableArrayList<GoodsItemViewModel> items = new ObservableArrayList<>();

    private ViewModelNavigator navigator;

    SearchResultViewModel(Activity context, String recommendKeyword ) {
        super(context);
        this.context = context;
        this.recommendKeyword.set(recommendKeyword);
        dataIsLoading.set(false);
    }

    public void cancel(){
        context.finish();
    }


    public void startGoodsSearch(String keyword) {
        dataIsLoading.set(true);
        new Thread(() -> {
            User user = UserDataRepository.getInstance().getThisUser();
            GoodsDataRepository.getInstance().goodsSearch(new DataSource.GeneralCallback<List<Goods>>() {
                @Override
                public void succeed(List<Goods> goodsList) {
                    ArrayList<GoodsItemViewModel> tempItems = new ArrayList<>();
                    for (Goods g : goodsList) {
                        tempItems.add(new GoodsItemViewModel(context, g));
                    }
                    dataIsLoading.set(false);

                    items.clear();
                    items.addAll(tempItems);

                    if (tempItems.size() > 0) {
                        dataIsEmpty.set(false);

                        if (navigator != null) {
                            navigator.showToast("搜索完成");
                        }
                    } else{
                        if (navigator != null) {
                            navigator.showToast("没有搜索到相关信息");
                        }
                    }


                }

                @Override
                public void failed(String log) {
                    dataIsLoading.set(false);

                    if (navigator != null) {
                        navigator.showToast( log);
                    }
                }
            }, keyword);
        }).start();
    }


    public void setNavigator(ViewModelNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigator = null;
    }
}
