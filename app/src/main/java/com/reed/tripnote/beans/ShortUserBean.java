package com.reed.tripnote.beans;

/**
 * 用户简要信息
 * Created by 伟 on 2016/3/17.
 */
public class ShortUserBean {

    private long userId;
    private String email;
    private String nickname;
    private String headImage;
    private int sex;
    private String introduction;
    private int liked;
    private int collections;
    private int travels;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getCollections() {
        return collections;
    }

    public void setCollections(int collections) {
        this.collections = collections;
    }

    public int getTravels() {
        return travels;
    }

    public void setTravels(int travels) {
        this.travels = travels;
    }

    @Override
    public String toString() {
        return "ShortUserBean{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headImage='" + headImage + '\'' +
                ", sex=" + sex +
                ", introduction='" + introduction + '\'' +
                ", liked=" + liked +
                ", collections=" + collections +
                ", travels=" + travels +
                '}';
    }
}
