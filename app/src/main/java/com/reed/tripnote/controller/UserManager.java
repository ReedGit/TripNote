package com.reed.tripnote.controller;

import android.content.Context;

import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.SharedPreferenceTool;

/**
 * 管理用户登录信息
 * Created by 伟 on 2016/3/18.
 */
public class UserManager {

    public static void loginUser(Context context, UserBean user) {
        SharedPreferenceTool prefTool = SharedPreferenceTool.getInstance(context, ConstantTool.USER);
        prefTool.putUserPref(user);
    }

    public static void exitLogin(Context context) {
        SharedPreferenceTool prefTool = SharedPreferenceTool.getInstance(context, ConstantTool.USER);
        prefTool.empty();
    }

}
