package com.lfork.a98620.lfree.data.community.local;

import com.lfork.a98620.lfree.data.community.CommunityDataSource;
import com.lfork.a98620.lfree.main.community.CommunityArticle;

import java.util.List;

public class CommunityLocalDataSource implements CommunityDataSource {

    private static List<CommunityArticle> itemViewModelList = null;

    public static List<CommunityArticle> getItemViewModelList() {
        return itemViewModelList;
    }

    public static void setItemViewModelList(List<CommunityArticle> itemViewModelList) {
        CommunityLocalDataSource.itemViewModelList = itemViewModelList;
    }

    @Override
    public void getArticleList(GeneralCallback callback) {}

    @Override
    public void getCommentList(GeneralCallback callback, int articleId) {}

    public static CommunityArticle getLocalArticle(int articlId) {
        CommunityArticle itemViewModel = null;
        for (CommunityArticle item : itemViewModelList) {
            if (item.getArticleId() == articlId) {
                itemViewModel = item;
                break;
            }
        }
        return itemViewModel;
    }
}
