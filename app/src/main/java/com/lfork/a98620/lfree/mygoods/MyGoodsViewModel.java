package com.lfork.a98620.lfree.mygoods;

import android.app.Activity;
import android.databinding.ObservableArrayList;
import android.text.format.DateFormat;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 98620 on 2018/5/5.
 */
public class MyGoodsViewModel extends BaseViewModel {
    public final ObservableArrayList<MyGoodsItemViewModel> items = new ObservableArrayList<>();

    private MyGoodsActivityNavigator navigator;

    MyGoodsViewModel(Activity context) {
        super(context);
    }

    @Override
    public void start() {
        getGoodsList();
    }

    @Override
    public void onDestroy() {
        navigator = null;
    }

    private void getGoodsList() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            User user = UserDataRepository.getInstance().getThisUser();
            if (user != null) {
                GoodsDataRepository.getInstance().getUserGoodsList(new DataSource.GeneralCallback<List<Goods>>() {
                    @Override
                    public void succeed(List<Goods> goodsList) {
                        ArrayList<MyGoodsItemViewModel> tempItems = new ArrayList<>();
                        for (Goods g : goodsList) {
                            MyGoodsItemViewModel viewModel = new MyGoodsItemViewModel(getContext(), g);
                            viewModel.setNavigator(navigator);
                            tempItems.add(viewModel);

                        }
                        dataIsLoading.set(false);

                        if (tempItems.size() > 0) {
                            dataIsEmpty.set(false);
                        } else {
                            dataIsEmpty.set(true);
                        }

                        items.clear();
                        items.addAll(tempItems);
//                        showToast("我的商品数据加载成功");
                    }

                    @Override
                    public void failed(String log) {
                        dataIsLoading.set(false);
                        dataIsEmpty.set(true);
                        showToast(log);
                    }
                }, DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString(), user.getUserId() + "");
            } else {
                showToast("可能没有网络");
            }
        }).start();
    }

    public void setNavigator(MyGoodsActivityNavigator navigator) {
        super.setNavigator(navigator);
        this.navigator = navigator;
    }

    @Override
    public void showToast(String msg) {
        super.showToast(msg);
    }
}
