package com.lfork.a98620.lfree.data.community.local;

import com.lfork.a98620.lfree.articlecontent.CommunityComment;
import com.lfork.a98620.lfree.data.community.CommunityDataSource;
import com.lfork.a98620.lfree.main.community.CommunityArticle;

import java.util.ArrayList;
import java.util.List;

public class CommunityLocalDataSource implements CommunityDataSource {

    private static List<CommunityArticle> articleList = new ArrayList<>();

    public static void setArticleList(List<CommunityArticle> articleList) {
        if (articleList.size()!= 0) {
            CommunityLocalDataSource.articleList.addAll(articleList);
        }
    }

    public static List<CommunityArticle> getArticleList() {
        return articleList;
    }

    @Override
    public void getArticleList(String fromTime, GeneralCallback callback) {}

    @Override
    public void getCommentList(GeneralCallback callback, int articleId) {}

    public static CommunityArticle getLocalArticle(int articlId) {
        CommunityArticle itemViewModel = null;
        for (CommunityArticle item : articleList) {
            if (item.getArticleId() == articlId) {
                itemViewModel = item;
                break;
            }
        }
        return itemViewModel;
    }
}
