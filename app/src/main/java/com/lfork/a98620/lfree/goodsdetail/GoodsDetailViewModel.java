package com.lfork.a98620.lfree.goodsdetail;

import android.content.Intent;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.common.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.source.GoodsDataRepository;
import com.lfork.a98620.lfree.databinding.GoodsDetailActBinding;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;

/**
 * Created by 98620 on 2018/4/13.
 */

public class GoodsDetailViewModel extends GoodsViewModel {

    public ObservableField<String> sellerImage = new ObservableField<>();

    public ObservableField<String> sellerName = new ObservableField<>();

    private int id;

    private int userId;

    private GoodsDetailActBinding binding;

    private GoodsDetailInfo g;

    private GoodsDataRepository repository;

    private AppCompatActivity context;

    GoodsDetailViewModel(AppCompatActivity context, int goodsId, GoodsDetailActBinding binding) {
        super(context);
        this.binding = binding;
        this.id = goodsId;
        this.context = context;
        refreshData();
    }

    private void refreshData() {
        repository = GoodsDataRepository.getInstance();

        new Thread(() -> {
            repository.getGoods(new DataSource.GeneralCallback<GoodsDetailInfo>() {
                @Override
                public void success(GoodsDetailInfo data) {
                    g = data;
                    context.runOnUiThread(() -> {
                        setData();
                    });
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

    private void setData() {
        if (g == null) return;
        name.set(g.getName());
        price.set("现价：" + g.getPrice());
        originPrice.set("原价：" + g.getOriginPrice());
        description.set(g.getDescription());
        sellerName.set(g.getUsername());
        publishDate.set(g.getPublishDate());
        sellerImage.set(g.getCoverImagePath());
        sellerImage.set(g.getUserPortraitPath());

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
        Intent intent = new Intent(context, ChatWindowActivity.class);
        intent.putExtra("user_id", userId);
        context.startActivity(intent);
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


}
