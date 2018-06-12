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
import com.lfork.a98620.lfree.util.ToastUtil;

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

                    if (tempItems.size() > 0) {
                        dataIsEmpty.set(false);
//                    items.addAll(data);
                        context.runOnUiThread(() -> {
                            ToastUtil.showShort(context, "搜索完成");
                            items.clear();
                            items.addAll(tempItems);
                            notifyChange();
                            navigator.notifyDataChanged();
                        });
                    } else{
                        context.runOnUiThread(() -> {
                            ToastUtil.showShort(context, "没有搜索到相关信息");
                            items.clear();
                            items.addAll(tempItems);
                            notifyChange();
                            navigator.notifyDataChanged();
                        });
                    }


                }

                @Override
                public void failed(String log) {
                    dataIsLoading.set(false);
                    context.runOnUiThread(() -> {
                        ToastUtil.showShort(context, log);
                    });

                }
            }, keyword);
        }).start();
    }

    @Override
    public ViewModelNavigator getNavigator() {
        return navigator;
    }

    public void setNavigator(ViewModelNavigator navigator) {
        this.navigator = navigator;
    }

}
