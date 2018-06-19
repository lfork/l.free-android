package com.lfork.a98620.lfree.goodsdetail;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.lfork.a98620.lfree.data.entity.Review;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.util.Config;

/**
 * Created by 98620 on 2018/6/13.
 */
public class ReviewItemViewModel {

    public final ObservableField<String> userPortraitPath = new ObservableField<>();

    public final ObservableField<String> index = new ObservableField<>();  //review id

    public final ObservableField<String> reviewerName = new ObservableField<>();

    public final ObservableField<String> content = new ObservableField<>();

    public final ObservableField<String> time = new ObservableField<>();

    public final ObservableBoolean isReviewedByThisUser = new ObservableBoolean(false);

    ReviewItemViewModel(Review review) {
        userPortraitPath.set(Config.ServerURL + "/image/" + review.getUser().getUserImagePath());
        index.set(review.getGoodsId());
        reviewerName.set(review.getUser().getUserName());
        time.set(review.getTime());
        content.set(review.getContent());
        int reviewerId = review.getUser().getUserId();
        if (reviewerId == UserDataRepository.getInstance().getUserId()) {
            isReviewedByThisUser.set(true);
        }

    }
}
