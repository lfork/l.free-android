package com.lfork.a98620.lfree.goodsdetail;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.lfork.a98620.lfree.base.BaseViewModel;
import com.lfork.a98620.lfree.data.base.entity.Review;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.base.Config;

import java.lang.ref.WeakReference;

/**
 * Created by 98620 on 2018/6/13.
 */
public class ReviewItemViewModel extends BaseViewModel{

    public final ObservableField<String> userPortraitPath = new ObservableField<>();

    public final ObservableField<String> index = new ObservableField<>();  //review id

    public final ObservableField<String> reviewerName = new ObservableField<>();

    public final ObservableField<String> content = new ObservableField<>();

    public final ObservableField<String> time = new ObservableField<>();

    public final ObservableBoolean isReviewedByThisUser = new ObservableBoolean(false);

    public final ObservableInt id = new ObservableInt();

    private WeakReference<GoodsDetailNavigator> navigator ;

    ReviewItemViewModel(Review review, GoodsDetailNavigator navigator) {
        this.navigator = new WeakReference<>(navigator);
        userPortraitPath.set(Config.ServerURL + "/image/" + review.getUser().getUserImagePath());
        index.set(review.getGoodsId());
        reviewerName.set(review.getUser().getUserName());
        time.set(review.getTime());
        content.set(review.getContent());
        id.set(review.getId());
        int reviewerId = review.getUser().getUserId();
        if (reviewerId == UserDataRepository.INSTANCE.getUserId()) {
            isReviewedByThisUser.set(true);
        }
    }


    public void deleteReview(){
        if (navigator != null && navigator.get() != null) {
            navigator.get().deleteReview(true, id.get());
        }

    }

    @Override
    public void setNavigator(Object navigator) {
        super.setNavigator(navigator);
    }
}
