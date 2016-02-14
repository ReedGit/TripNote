package com.reed.tripnote.beans;

public class ContentImageBean {

    private long imageId;
    private long contentId;
    private String imageUrl;

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ContentImageBean{" +
                "imageId=" + imageId +
                ", contentId=" + contentId +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
