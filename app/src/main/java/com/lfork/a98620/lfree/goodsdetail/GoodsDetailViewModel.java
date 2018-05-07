package com.lfork.a98620.lfree.goodsdetail;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lfork.a98620.lfree.base.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.databinding.GoodsDetailActBinding;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;
import com.lfork.a98620.lfree.util.Config;
import com.lfork.a98620.lfree.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by 98620 on 2018/4/13.
 */

/**
 *   数据加载模型
 *
 *   正常的加载模型：
 *
 *   异常情况的模型加载：
 */

public class GoodsDetailViewModel extends GoodsViewModel {

    public final ObservableField<String> sellerImage = new ObservableField<>();

    public final ObservableField<String> sellerName = new ObservableField<>();

    public final ObservableArrayList<String> reviewItems = new ObservableArrayList<>();

    private int id;

    private int userId;

    private ViewModelNavigator navigator;

    private GoodsDetailActBinding binding;

    private GoodsDetailInfo g;

    private GoodsDataRepository repository;

    private AppCompatActivity context;

    GoodsDetailViewModel(AppCompatActivity context, int goodsId, GoodsDetailActBinding binding) {
        super(context);
        this.binding = binding;
        this.id = goodsId;
        this.context = context;
    }

    @Override
    public void start() {
        getNormalData();

    }


    private void getNormalData() {
        repository = GoodsDataRepository.getInstance();

        new Thread(() -> {
            repository.getGoods(new DataSource.GeneralCallback<GoodsDetailInfo>() {
                @Override
                public void succeed(GoodsDetailInfo data) {
                    g = data;
                    ArrayList<String> reviews = data.getReviews();
                    if (reviews.size() > 0) {
                        context.runOnUiThread(() -> {
                            setData();
                            reviewItems.addAll(reviews);
                            navigator.notifyDataChanged();
                        });
                    } else {
                        context.runOnUiThread(() -> {
                            setData();
                        });
                    }
                }

                @Override
                public void failed(String log) {
                    context.runOnUiThread(() -> {
                        Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show();
                    });
                }
            }, id);
        }).start();
    }

//    private void getReviews(){
//        new Thread(() -> {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            User user = UserDataRepository.getInstance().getThisUser();
//            GoodsDataRepository.getInstance().goodsSearch(new DataSource.GeneralCallback<List<Review>>() {
//                @Override
//                public void succeed(List<Review> goodsList) {
//                    ArrayList<Review> tempItems = new ArrayList<>();
//                    for (Review g : goodsList) {
//                        tempItems.add(new Review());
//                    }
//                    dataIsLoading.set(false);
//
//                    if (tempItems.size() > 0) {
//                        dataIsEmpty.set(false);
////                    items.addAll(data);
//                        context.runOnUiThread(() -> {
//                            ToastUtil.showShort(context, "搜索完成");
//                            reviewItems.clear();
//                            reviewItems.addAll(tempItems);
//                            notifyChange();
//                            navigator.notifyDataChanged();
//                        });
//                    } else{
//                        context.runOnUiThread(() -> {
//                            ToastUtil.showShort(context, "没有搜索到相关信息");
//                            reviewItems.clear();
//                            reviewItems.addAll(tempItems);
//                            notifyChange();
//                            navigator.notifyDataChanged();
//                        });
//                    }
//
//
//                }
//
//                @Override
//                public void failed(String log) {
//                    dataIsLoading.set(false);
//                    context.runOnUiThread(() -> {
//                        ToastUtil.showShort(context, log);
//                    });
//
//                }
//            }, keyword);
//        }).start();
//    }

    private void setData() {
        if (g == null) return;
        name.set(g.getName());
        price.set("现价：" + g.getPrice());
        originPrice.set("原价：" + g.getOriginPrice());
        description.set(g.getDescription());
        sellerName.set(g.getUsername());
        publishDate.set(g.getPublishDate());
        sellerImage.set(Config.ServerURL + "/image" + g.getUserPortraitPath());

        if (g.getImages() != null) {
            String[] imagesStr = g.getImages();
            for (String image : imagesStr){
                images.add("http://www.lfork.top/image" + image);
            }
        }
        binding.banner.update(images);      //刷新轮播图
        userId = g.getUserId();
    }


    public void startPrivateChat() {

        UserDataRepository userDataRepository = UserDataRepository.getInstance();
        User u = userDataRepository.getThisUser();
        if (u != null) {
             if (u.getUserId() == userId){
                 ToastUtil.showShort(context, "不能和自己聊天");
                 return;
             }
            Intent intent = new Intent(context, ChatWindowActivity.class);
            intent.putExtra("username", sellerName.get());
            intent.putExtra("user_id", userId);
            context.startActivity(intent);
        } else {
            ToastUtil.showShort(context, "IM模块正在初始化...");
        }

    }

    public void publishReview() {

    }

    public void openSellerInfo() {

        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("user_id", userId);
        context.startActivity(intent);

    }

    public void openImageDetail() {

    }


    @Override
    public void setNavigator(ViewModelNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void destroy() {

    }
}
