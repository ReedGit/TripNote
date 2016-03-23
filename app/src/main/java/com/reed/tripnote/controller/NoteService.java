package com.reed.tripnote.controller;

import com.reed.tripnote.beans.UserBean;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

    @FormUrlEncoded
    @POST("user/{id}")
    Call<JSONObject> setProfile(@Path("id") long id, @FieldMap Map<String, Object> map);

    @GET("user/exist")
    Call<JSONObject> exist(@Query("email") String email);

    @FormUrlEncoded
    @POST("user/reset")
    Call<JSONObject> reset(@Field("email") String email);

    @Multipart
    @POST("user/{id}/change_avatar")
    Call<JSONObject> uploadImage(@Path("id") long id, @Part("headImage") String name);
}
