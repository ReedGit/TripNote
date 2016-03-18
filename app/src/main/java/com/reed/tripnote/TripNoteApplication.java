package com.reed.tripnote;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.controller.UserManager;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;
import com.reed.tripnote.tools.RetrofitTool;
import com.reed.tripnote.tools.SharedPreferenceTool;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripNoteApplication extends Application {

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        user = SharedPreferenceTool.getInstance(this, ConstantTool.USER).getUserPref();
        if (user == null) {
            return;
        }
        String email = user.getEmail();
        String password = user.getPassword();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            Call<JSONObject> call = RetrofitTool.getService().login(email, password);
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    JSONObject result = response.body();
                    try {
                        if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                            return;
                        }
                        UserBean user = FormatTool.gson.fromJson(String.valueOf(result.getJSONObject(ConstantTool.DATA)), UserBean.class);
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
            user = null;
            UserManager.exitLogin(this);
        }

    }
}
