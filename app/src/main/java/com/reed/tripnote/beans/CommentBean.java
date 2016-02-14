package com.reed.tripnote.beans;

import java.util.Date;

public class CommentBean {

    private long commentId;
    private long userId;
    private Date time;
    private String remark;
    private long travelId;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", time=" + time +
                ", remark='" + remark + '\'' +
                ", travelId=" + travelId +
                '}';
    }
}
