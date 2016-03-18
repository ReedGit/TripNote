package com.reed.tripnote.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.reed.tripnote.beans.UserBean;

/**
 * sharePreference工具类
 * Created by 伟 on 2016/3/18.
 */
public class SharedPreferenceTool {

    private SharedPreferences pref;
    private static SharedPreferenceTool prefTool;

    public SharedPreferenceTool(Context context, String name) {
        pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferenceTool getInstance(Context context, String name) {
        if (prefTool == null) {
            prefTool = new SharedPreferenceTool(context, name);
        }
        return prefTool;
    }

    public void putUserPref(UserBean user) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(ConstantTool.USER_ID, user.getUserId());
        editor.putString(ConstantTool.EMAIL, user.getEmail());
        editor.putString(ConstantTool.PASSWORD, user.getPassword());
        editor.putString(ConstantTool.NICKNAME, user.getNickname());
        editor.putString(ConstantTool.HEAD_IMAGE, user.getHeadImage());
        editor.putInt(ConstantTool.SEX, user.getSex());
        editor.putString(ConstantTool.INTRODUCTION, user.getIntroduction());
        editor.apply();
    }

    public UserBean getUserPref() {
        UserBean user = new UserBean();
        user.setUserId(pref.getLong(ConstantTool.USER_ID, 0));
        if (user.getUserId() == 0) {
            return null;
        }
        user.setEmail(pref.getString(ConstantTool.EMAIL, null));
        user.setHeadImage(pref.getString(ConstantTool.HEAD_IMAGE, null));
        user.setIntroduction(pref.getString(ConstantTool.INTRODUCTION, null));
        user.setPassword(pref.getString(ConstantTool.PASSWORD, null));
        user.setNickname(pref.getString(ConstantTool.NICKNAME, null));
        user.setSex(pref.getInt(ConstantTool.SEX, 2));
        return user;
    }

    public void empty() {
        pref.edit().clear().apply();
    }

}
