package com.reed.tripnote.tools;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.reed.tripnote.App;

/**
 * Toast显示工具类
 * Created by 伟 on 2016/3/17.
 */
public class ToastTool {

    public static void show(String msg) {
        Toast.makeText(App.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(@StringRes int resId) {
        Toast.makeText(App.getContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
