package com.reed.tripnote.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 数据格式转换工具类
 * Created by 伟 on 2016/2/14.
 */
public class FormatTool {

    public static boolean isEmail(String email){
        String regex = "^[a-z0-9]+([._\\\\\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        return email.matches(regex);
    }

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            })
            .create();

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public static SimpleDateFormat shortFormat = new SimpleDateFormat("MM.dd HH:mm", Locale.CHINA);

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);

    //将字符串转换成时间
    public static Date transformToDate(String date) {
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //将时间转换成字符串
    public static String transformToString(Date date) {
        return format.format(date);
    }

    public static String simpleTransformToString(Date date) {
        return shortFormat.format(date);
    }

    public static String transformToDateString(Date date) {
        return dateFormat.format(date);
    }
}
