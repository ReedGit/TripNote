package com.reed.tripnote.tools;

import com.reed.tripnote.controller.NoteService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * retrofit工具类
 * Created by 伟 on 2016/3/17.
 */
public class RetrofitTool {

    private volatile static Retrofit retrofit;
    private volatile static NoteService service;

    private static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (RetrofitTool.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(ConstantTool.baseUrl)
                                    //解析结果为需要的类型
                            .addConverterFactory(JsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    public static NoteService getService() {
        if (service == null) {
            synchronized (RetrofitTool.class) {
                if (service == null) {
                    service = getInstance().create(NoteService.class);
                }
            }
        }
        return service;
    }
}
