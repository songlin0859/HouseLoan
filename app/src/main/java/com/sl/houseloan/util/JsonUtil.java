package com.sl.houseloan.util;

import com.google.gson.Gson;

/**
 * Created by 36128 on 2017/12/9.
 */

public class JsonUtil {
    private static final Gson sGson;
    static {
        sGson=new Gson();
    }
    public static String toJson(Object object){
        return sGson.toJson(object);
    }

    public static <T>T fromJson(String json,Class<T> clz){
        return sGson.fromJson(json,clz);
    }
}
