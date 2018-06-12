package com.lfork.a98620.lfree.data.community;

import com.lfork.a98620.lfree.data.DataSource;

public interface CommunityDataSource extends DataSource {
    void getArticleList(String fromTime, GeneralCallback callback);
    void getCommentList(GeneralCallback callback, int articleId);
}
