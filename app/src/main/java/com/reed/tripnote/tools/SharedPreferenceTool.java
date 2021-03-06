package com.reed.tripnote.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.reed.tripnote.App;
import com.reed.tripnote.beans.UserBean;

/**
 * sharePreference工具类
 * Created by 伟 on 2016/3/18.
 */
public class SharedPreferenceTool {

    private SharedPreferences pref;
    private static SharedPreferenceTool prefTool;

    public SharedPreferenceTool(String name) {
        pref = App.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferenceTool getInstance(String name) {
        if (prefTool == null) {
            prefTool = new SharedPreferenceTool(name);
        }
        return prefTool;
    }

    public void putUserPref(UserBean user) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(ConstantTool.USER_ID, user.getUserId());
        editor.putString(ConstantTool.EMAIL, user.getEmail());
        editor.putString(ConstantTool.PASSWORD, user.getPassword());
        editor.putString(ConstantTool.NICKNAME, user.getNickName());
        editor.putString(ConstantTool.HEAD_IMAGE, user.getHeadImage());
        editor.putInt(ConstantTool.SEX, user.getSex());
        editor.putString(ConstantTool.INTRODUCTION, user.getIntroduction());
        editor.putString(ConstantTool.TOKEN, user.getToken());
        editor.apply();
    }

    public UserBean getUserPref() {
        UserBean user = new UserBean();
        user.setUserId(pref.getLong(ConstantTool.USER_ID, -1));
        if (user.getUserId() == -1) {
            return null;
        }
        user.setEmail(pref.getString(ConstantTool.EMAIL, null));
        user.setHeadImage(pref.getString(ConstantTool.HEAD_IMAGE, null));
        user.setIntroduction(pref.getString(ConstantTool.INTRODUCTION, null));
        user.setPassword(pref.getString(ConstantTool.PASSWORD, null));
        user.setNickName(pref.getString(ConstantTool.NICKNAME, null));
        user.setSex(pref.getInt(ConstantTool.SEX, 2));
        user.setToken(pref.getString(ConstantTool.TOKEN, null));
        return user;
    }

    public void empty() {
        pref.edit().clear().apply();
    }

}
