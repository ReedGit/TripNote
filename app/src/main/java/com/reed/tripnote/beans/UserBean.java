package com.reed.tripnote.beans;


public class UserBean {

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    private long userId;
    private String email;
    private String password;
    private String nickName;
    private String headImage;
    private int sex;
    private String introduction;
    private String token;
    private int liked;
    private int collection;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getTravels() {
        return travels;
    }

    public void setTravels(int travels) {
        this.travels = travels;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImage='" + headImage + '\'' +
                ", sex=" + sex +
                ", introduction='" + introduction + '\'' +
                ", token='" + token + '\'' +
                ", liked=" + liked +
                ", collection=" + collection +
                ", travels=" + travels +
                '}';
    }
}
