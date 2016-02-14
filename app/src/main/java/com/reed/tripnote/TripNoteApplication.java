package com.reed.tripnote;

import android.app.Application;

import com.reed.tripnote.beans.UserBean;

public class TripNoteApplication extends Application {

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
