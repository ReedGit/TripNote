package com.reed.tripnote.controller;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * retrofit service interface
 * Created by 伟 on 2016/3/17.
 */
public interface NoteService {

    //the api for user's login
    @FormUrlEncoded
    @POST("user/login")
    Call<JSONObject> login(@Field("email") String email, @Field("password") String password);

    //the api for user's register
    @FormUrlEncoded
    @POST("user/register")
    Call<JSONObject> register(@Field("email") String email, @Field("password") String password);

    //the api for user to modify personal's information
    @FormUrlEncoded
    @POST("user/{id}")
    Call<JSONObject> setProfile(@Path("id") long id, @FieldMap Map<String, Object> map);

    //the api for forgetting password
    @FormUrlEncoded
    @POST("user/reset")
    Call<JSONObject> reset(@Field("email") String email);

    //the api for getting user's information
    @GET("user/{id}")
    Call<JSONObject> getUser(@Path("id") long id);

    //the api for modifying user's portrait
    @Multipart
    @POST("user/{id}/change_avatar")
    Call<JSONObject> uploadImage(@Path("id") long id, @PartMap Map<String, RequestBody> map, @Part("token") String token);

    //the api for getting the travels in the main page
    @GET("travel/explore")
    Call<JSONObject> getTravels(@Query("page") int page);

    @GET("travel/travels/{userId}")
    Call<JSONObject> getUserTravel(@Path("userId") long userId, @Query("page") int page);

    //the api for searching the travel by the key
    @GET("travel/search")
    Call<JSONObject> searchTravel(@Query("q") String q, @Query("page") int page);

    //the api for delete user's travel
    @GET("travel/delete")
    Call<JSONObject> deleteTravel(@QueryMap Map<String, Object> map);

    //the api for creating a travel
    @Multipart
    @POST("travel/create")
    Call<JSONObject> createTravel(@PartMap Map<String, Object> map, @PartMap Map<String, RequestBody> coverMap);

    @GET("travel/content/detail")
    Call<JSONObject> getContent(@Query("travelId") long travelId);

    @GET("travel/total/{travelId}")
    Call<JSONObject> getCount(@Path("travelId") long travelId, @QueryMap Map<String, Object> map);

    @Multipart
    @POST("travel/content/create")
    Call<JSONObject> createContent(@PartMap Map<String, Object> map, @Part() List<MultipartBody.Part> imageMap);

    //the api for adding comment
    @FormUrlEncoded
    @POST("comment/add")
    Call<JSONObject> addComment(@FieldMap Map<String, Object> map);

    //the api for getting comment
    @GET("comment/list")
    Call<JSONObject> getComments(@Query("travelId") long travelId, @Query("page") int page);

    @FormUrlEncoded
    @POST("like/like")
    Call<JSONObject> like(@Field("travelId") long travelId, @Field("userId") long userId);

    @FormUrlEncoded
    @POST("like/cancel")
    Call<JSONObject> cancelLike(@Field("travelId") long travelId, @Field("userId") long userId);

    @GET("like/travels")
    Call<JSONObject> userLike(@Query("userId") long userId, @Query("page") int page);

    @FormUrlEncoded
    @POST("collection/collect")
    Call<JSONObject> collect(@Field("travelId") long travelId, @Field("userId") long userId);

    @FormUrlEncoded
    @POST("collection/cancel")
    Call<JSONObject> cancelCollect(@Field("travelId") long travelId, @Field("userId") long userId);

    //the api for getting the travels collected by user
    @GET("collection/travels")
    Call<JSONObject> userCollection(@Query("userId") long userId, @Query("page") int page);

}
