package com.lfork.a98620.lfree.mygoods;

import android.app.Activity;
import android.databinding.ObservableArrayList;
import android.os.Looper;
import android.text.format.DateFormat;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 98620 on 2018/5/5.
 */
public class MyGoodsViewModel extends BaseViewModel {

    private Activity context;

    public ObservableArrayList<MyGoodsItemViewModel> items = new ObservableArrayList<>();

    private MyGoodsActivityNavigator navigator;

    MyGoodsViewModel(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    public void start() {
        getGoodsList();
    }

    @Override
    public void destroy() {

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
                            tempItems.add(new MyGoodsItemViewModel(context, g));
                        }
                        dataIsLoading.set(false);

                        if (tempItems.size() > 0)
                            dataIsEmpty.set(false);
//                    items.addAll(data);
                        context.runOnUiThread(() -> {
                            ToastUtil.showShort(context, "我的商品数据加载成功");
                            items.clear();
                            items.addAll(tempItems);
                            notifyChange();
                            navigator.notifyDataChanged();
                        });

                    }

                    @Override
                    public void failed(String log) {
                        dataIsLoading.set(false);
                        context.runOnUiThread(() -> {
                            ToastUtil.showShort(context, log);
                        });

                    }
                }, DateFormat.format("yyyy-MM-dd HH:mm:ss", new Date()).toString(), user.getUserId() + "");
            } else {
                Looper.prepare();//???
                ToastUtil.showShort(context, "可能没有网络");
            }
        }).start();
    }

    @Override
    public MyGoodsActivityNavigator getNavigator() {
        return navigator;
    }

    public void setNavigator(MyGoodsActivityNavigator navigator) {
        this.navigator = navigator;
    }
}
