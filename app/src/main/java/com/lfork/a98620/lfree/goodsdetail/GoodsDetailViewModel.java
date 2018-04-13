package com.lfork.a98620.lfree.goodsdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.chatwindow.ChatWindowActivity;
import com.lfork.a98620.lfree.common.BaseViewModel;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.source.GoodsDataRepository;
import com.lfork.a98620.lfree.userinfo.UserInfoActivity;

/**
 * Created by 98620 on 2018/4/13.
 */

public class GoodsDetailViewModel extends BaseViewModel {


    public ObservableField<String> name = new ObservableField<>();

    public ObservableField<String> publishDate = new ObservableField<>();

    public ObservableField<String> price = new ObservableField<>();

    public ObservableField<String> originPrice = new ObservableField<>();

    public ObservableField<String> description = new ObservableField<>();

    public ObservableField<String> type = new ObservableField<>();

    public ObservableField<String> image1 = new ObservableField<>();

    public ObservableField<String> image2 = new ObservableField<>();

    public ObservableField<String> image3 = new ObservableField<>();

    public ObservableField<String> image4 = new ObservableField<>();

    public ObservableField<String> image5 = new ObservableField<>();

    public ObservableField<String> sellerImage = new ObservableField<>();

    public ObservableField<String> sellerName = new ObservableField<>();

    private int id;

    private int userId;

    private int categoryId;

    private GoodsDataRepository repository;

    public GoodsDetailViewModel(Context context, int goodsId, int categoryId) {

        super(context);
        this.categoryId = categoryId;
        this.id = goodsId;
        initData();
    }

    private void initData() {
        repository = GoodsDataRepository.getInstance();

        Goods g = repository.getGoods(categoryId, id);

        if (g == null) return;

        name.set(g.getName());
        price.set("现价：" + g.getPrice());
        originPrice.set("原价：" + g.getOriginPrice());
        description.set(g.getDescription());
        sellerName.set(g.getUserId() + "");
        publishDate.set(g.getPublishDate());
        sellerImage.set(g.getCoverImagePath());
    }

    public void startPrivateChat() {
        Intent intent = new Intent(getContext(), ChatWindowActivity.class);
        intent.putExtra("seller_id", userId);
        getContext().startActivity(intent);
    }

    public void publishReview() {

    }

    public void openSellerInfo() {

        Intent intent = new Intent(getContext(), UserInfoActivity.class);
        intent.putExtra("seller_id", userId);
        getContext().startActivity(intent);

    }

    public void openImageDetail() {

    }


}
