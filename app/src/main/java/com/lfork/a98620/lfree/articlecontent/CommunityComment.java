package com.lfork.a98620.lfree.articlecontent;

import com.lfork.a98620.lfree.base.BaseViewModel;

public class CommunityComment extends BaseViewModel {
    private int articleId;
    private String comment;
    private String commentTime;
    private int reviewerId;
    private String reviewer;

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public void setReviewerId(int reviewerID) {
        this.reviewerId = reviewerID;
    }

    public int getArticleId() {
        return articleId;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }
}
