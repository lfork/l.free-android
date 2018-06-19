package com.lfork.a98620.lfree.goodsdetail;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.lfork.a98620.lfree.base.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.base.viewmodel.ViewModelNavigator;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.GoodsDetailInfo;
import com.lfork.a98620.lfree.data.entity.Review;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.util.Config;
import com.lfork.a98620.lfree.util.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 98620 on 2018/4/13.
 * 数据加载模型
 * <p>
 * 正常的加载模型：
 * <p>
 * 异常情况的模型加载：
 */

public class GoodsDetailViewModel extends GoodsViewModel {

    public final ObservableField<String> sellerImage = new ObservableField<>();

    public final ObservableField<String> sellerName = new ObservableField<>();

    public final ObservableArrayList<ReviewItemViewModel> reviewItems = new ObservableArrayList<>();

    public final ObservableField<String> review = new ObservableField<>("");

    public final ObservableBoolean reviewDataIsEmpty = new ObservableBoolean(false);

    private boolean initialized = false;


    private int id;

    private int userId;

    private GoodsDetailNavigator navigator;

    private GoodsDetailInfo g;

    GoodsDetailViewModel(Context context, int goodsId) {
        super(context);
        this.id = goodsId;
        this.context = context;
    }

    @Override
    public void start() {
        if (!initialized) {
            getNormalData();
            initialized = true;
        }
    }

    private void getNormalData() {
        new Thread(() -> GoodsDataRepository.getInstance().getGoods(new DataSource.GeneralCallback<GoodsDetailInfo>() {
            @Override
            public void succeed(GoodsDetailInfo data) {
                g = data;
                ArrayList<Review> reviews = data.getReviews();
                if (reviews.size() > 0) {
                    reviewItems.clear();
                    Collections.reverse(reviews);
                    for (Review r: reviews) {
                        ReviewItemViewModel viewModel = new ReviewItemViewModel(r);
                        reviewItems.add(0, viewModel);
                    }
                    setData();
                    if (navigator != null) {
                        navigator.notifyReviewChanged();
                    }
                } else {
                    reviewDataIsEmpty.set(true);  //这个data指的是review
                    setData();
                }
            }

            @Override
            public void failed(String log) {
                showMessage("加载失败:" + log);
            }
        }, id)).start();
    }

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
            for (String image : imagesStr) {
                images.add("http://www.lfork.top/image" + image);
            }
        }

        if (g.getUserId() == UserDataRepository.getInstance().getUserId()) {
            if (navigator != null) {
                navigator.setActionBar();
            }
        }

        userId = g.getUserId();

        if (navigator != null) {
            navigator.refreshBanner(images);
        }
    }

    public void startPrivateChat() {
        UserDataRepository userDataRepository = UserDataRepository.getInstance();
        User u = userDataRepository.getThisUser();
        if (u != null) {
            if (u.getUserId() == userId) {
                showMessage("不能和自己聊天");
                return;
            }

            navigator.openChatWindow(sellerName.get(), userId);

        } else {
            showMessage("IM模块正在初始化...");
        }

    }

    public void addReview() {
        if (navigator != null) {
            navigator.closeSoftKeyBoard();
        }
        if (TextUtils.isEmpty(review.get())) {
            return;
        }

        Review r = new Review(review.get());
        r.setGoodsId(id + "");
        r.setUserId(UserDataRepository.getInstance().getThisUser().getUserId() + "");
        r.setUser(UserDataRepository.getInstance().getThisUser());
        r.setTime(TimeUtil.getStandardTime());
        new Thread(() -> GoodsDataRepository.getInstance().addReview(new DataSource.GeneralCallback<Review>() {
            @Override
            public void succeed(Review data) {

                ReviewItemViewModel viewModel = new ReviewItemViewModel(data);
                reviewItems.add(0,viewModel );
                review.set("");

                if (navigator != null) {
                    navigator.notifyReviewChanged();
                }
                reviewDataIsEmpty.set(false);
                showMessage("评论成功");


            }

            @Override
            public void failed(String log) {
                showMessage(log);

            }
        }, r)).start();

    }

    public void openSellerInfo() {
        if (navigator != null) {
            navigator.openUserInfo(userId);
        }
    }


    public void onClickImage(int position) {
        navigator.openBigImages(images, position);
    }

    @Override
    public void setNavigator(ViewModelNavigator navigator) {
        this.navigator = (GoodsDetailNavigator) navigator;
    }

    @Override
    public void destroy() {
        navigator = null;
    }

    private void showMessage(String msg) {

        if (navigator != null) {
            navigator.showMessage(msg);
        }
    }
}
