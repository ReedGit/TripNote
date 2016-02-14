package com.reed.tripnote.beans;

public class CollectionBean {

    private long collectionId;
    private long userId;
    private long travelId;

    public long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(long collectionId) {
        this.collectionId = collectionId;
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
        return "CollectionBean{" +
                "collectionId=" + collectionId +
                ", userId=" + userId +
                ", travelId=" + travelId +
                '}';
    }
}
