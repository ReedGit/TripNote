package com.reed.tripnote.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ContentBean implements Serializable {

    private long contentId;
    private String article;
    private String location;
    private Date time;
    private long travelId;
    private String coordinate;
    private int day;
    private List<String> imageurl;

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getTravelId() {
        return travelId;
    }

    public void setTravelId(long travelId) {
        this.travelId = travelId;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<String> getImageurl() {
        return imageurl;
    }

    public void setImageurl(List<String> imageurl) {
        this.imageurl = imageurl;
    }

    @Override
    public String toString() {
        return "ContentBean{" +
                "contentId=" + contentId +
                ", article='" + article + '\'' +
                ", location='" + location + '\'' +
                ", time=" + time +
                ", travelId=" + travelId +
                ", coordinate='" + coordinate + '\'' +
                ", day=" + day +
                ", imageurl=" + imageurl +
                '}';
    }
}
