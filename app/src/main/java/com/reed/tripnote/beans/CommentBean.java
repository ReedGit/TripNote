package com.reed.tripnote.beans;

import java.util.Date;

public class CommentBean {

    private long commentId;
    private long userId;
    private Date time;
    private String remark;
    private long travelId;
    private String nickName;
    private String headImage;
    private String title;

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

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", time=" + time +
                ", remark='" + remark + '\'' +
                ", travelId=" + travelId +
                ", nickName='" + nickName + '\'' +
                ", headImage='" + headImage + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
