package com.lfork.a98620.lfree.data.community.local;

import android.os.Handler;
import android.os.Message;

import com.lfork.a98620.lfree.data.community.CommunityDataSource;
import com.lfork.a98620.lfree.main.community.CommunityComment;
import com.lfork.a98620.lfree.main.community.CommunityFragmentItemViewModel;
import com.lfork.a98620.lfree.main.community.articlecontent.CommentView;

import java.util.List;

public class CommunityLocalDataSource implements CommunityDataSource {

    private static List<CommunityFragmentItemViewModel> itemViewModelList = null;

    public static List<CommunityFragmentItemViewModel> getItemViewModelList() {
        return itemViewModelList;
    }

    public static void setItemViewModelList(List<CommunityFragmentItemViewModel> itemViewModelList) {
        CommunityLocalDataSource.itemViewModelList = itemViewModelList;
    }

    @Override
    public void getArticleList(GeneralCallback callback) {}

    @Override
    public void getCommentList(GeneralCallback callback, int articleId) {}

    public static CommunityFragmentItemViewModel getLocalArticle(int articlId) {
        CommunityFragmentItemViewModel itemViewModel = null;
        for (CommunityFragmentItemViewModel item : itemViewModelList) {
            if (item.getArticleId() == articlId) {
                itemViewModel = item;
                break;
            }
        }
        return itemViewModel;
    }
}
