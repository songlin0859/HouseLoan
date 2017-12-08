package com.sl.houseloan.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferencs
 * Created by csl on 2017/12/8.
 */

public class SpUtil {
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static Context sContext;
    private static SharedPreferences sSP;
    public static void init(Context context){
        sContext=context.getApplicationContext();
        sSP=sContext.getSharedPreferences("sp",Context.MODE_PRIVATE);
    }

    private static void putInt(String key,int value){
        sSP.edit().putInt(key,value).apply();
    }

    private static void putFloat(String key,float value){
        sSP.edit().putFloat(key,value).apply();
    }

    private static void putLong(String key,long value){
        sSP.edit().putLong(key,value).apply();
    }
    /*----------------------------------*/
    public static void saveYear(int year){
        putInt(YEAR,year);
    }
    public static int getYear(int defaultValue){
        return sSP.getInt(YEAR,defaultValue);
    }

    public static void saveMonth(int month){
        putInt(MONTH,month);
    }
    public static int getMonth(int defaultValue){
        return sSP.getInt(MONTH,defaultValue);
    }

    public static void saveDay(int day){
        putInt(DAY,day);
    }
    public static int getDay(int defaultValue){
        return sSP.getInt(DAY,defaultValue);
    }
}
