package com.reed.tripnote.controller;

import com.reed.tripnote.beans.UserBean;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * retrofit service接口
 * Created by 伟 on 2016/3/17.
 */
public interface NoteService {

    @FormUrlEncoded
    @POST("user/login")
    Call<JSONObject> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/register")
    Call<JSONObject> register(@Field("email") String email, @Field("password") String password);

    @POST("user/setProfile")
    Call<JSONObject> setProfile(@Body UserBean user);

    @GET("user/exist")
    Call<JSONObject> exist(@Query("email") String email);
}
