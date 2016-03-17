package com.reed.tripnote.beans;

public class LikeBean {

    private long likeId;
    private long userId;
    private long travelId;
    private String nickname;
    private String userImage;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    @Override
    public String toString() {
        return "LikeBean{" +
                "likeId=" + likeId +
                ", userId=" + userId +
                ", travelId=" + travelId +
                ", nickname='" + nickname + '\'' +
                ", userImage='" + userImage + '\'' +
                '}';
    }
}
