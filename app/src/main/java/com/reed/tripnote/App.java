package com.reed.tripnote;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.controller.UserManager;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;
import com.reed.tripnote.tools.LogTool;
import com.reed.tripnote.tools.RetrofitTool;
import com.reed.tripnote.tools.SharedPreferenceTool;
import com.reed.tripnote.tools.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class App extends Application {

    private static final String TAG = App.class.toString();

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        user = SharedPreferenceTool.getInstance(this, ConstantTool.USER).getUserPref();
        if (user == null) {
            return;
        }
        LogTool.i(TAG, user.toString());
        String email = user.getEmail();
        final String password = user.getPassword();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            Call<JSONObject> call = RetrofitTool.getService().login(email, password);
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    if (response.code() != 200) {
                        LogTool.e(TAG, response.message());
                        ToastTool.show(context, response.message());
                        return;
                    }
                    JSONObject result = response.body();
                    try {
                        if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                            ToastTool.show(context, "用户信息过期，请重新登录");
                            LogTool.i(TAG, result.toString());
                            UserManager.exitLogin(context);
                            return;
                        }
                        UserBean user = FormatTool.gson.fromJson(String.valueOf(result.getJSONObject(ConstantTool.DATA)), UserBean.class);
                        user.setPassword(password);
                        UserManager.loginUser(context, user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {

                }
            });
        } else {
            UserManager.exitLogin(context);
        }

    }
}
