package com.reed.tripnote.controller;

import android.content.Context;

import com.reed.tripnote.App;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.SharedPreferenceTool;

/**
 * 管理用户登录信息
 * Created by 伟 on 2016/3/18.
 */
public class UserManager {

    public static void loginUser(UserBean user) {
        ((App) App.getContext()).setUser(user);
        SharedPreferenceTool prefTool = SharedPreferenceTool.getInstance(ConstantTool.USER);
        prefTool.putUserPref(user);
    }

    public static void exitLogin() {
        ((App) App.getContext()).setUser(null);
        SharedPreferenceTool prefTool = SharedPreferenceTool.getInstance(ConstantTool.USER);
        prefTool.empty();
    }

}
