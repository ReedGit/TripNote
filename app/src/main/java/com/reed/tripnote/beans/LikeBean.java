package com.reed.tripnote.beans;

public class LikeBean {

    private long likeId;
    private long userId;
    private long travelId;

    public long getLikeId() {
        return likeId;
    }

    public void setLikeId(long likeId) {
        this.likeId = likeId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    @Override
    public String toString() {
        return "LikeBean{" +
                "likeId=" + likeId +
                ", userId=" + userId +
                ", travelId=" + travelId +
                '}';
    }
}
