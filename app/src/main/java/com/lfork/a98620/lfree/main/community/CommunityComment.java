package com.lfork.a98620.lfree.main.community;

public class CommunityComment {
    private int articleId;
    private String comment;
    private String commentTime;
    private int reviewerId;

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
}
