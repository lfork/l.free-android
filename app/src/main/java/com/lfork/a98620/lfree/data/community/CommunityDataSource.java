package com.lfork.a98620.lfree.data.community;

import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.main.community.CommunityFragmentItemViewModel;

public interface CommunityDataSource extends DataSource {
    void getArticleList(GeneralCallback callback);
    void getCommentList(GeneralCallback callback, int articleId);
}
