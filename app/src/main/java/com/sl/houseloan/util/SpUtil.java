package com.sl.houseloan.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sl.houseloan.LoanBean;

/**
 * SharedPreferencs
 * Created by csl on 2017/12/8.
 */

public class SpUtil {
    private static final String LOAN_INFO = "loan_info";
    private static Context sContext;
    private static SharedPreferences sSP;
    public static void init(Context context){
        sContext=context.getApplicationContext();
        sSP=sContext.getSharedPreferences("sp",Context.MODE_PRIVATE);
    }

    private static void putString(String key,String value){
        sSP.edit().putString(key,value).apply();
    }

    /*--------------------------*/
    public static void saveLoanInfo(LoanBean loanBean){
        putString(LOAN_INFO,JsonUtil.toJson(loanBean));
    }

    public static LoanBean getLoanInfo(){
        try {
            LoanBean loanBean = JsonUtil.fromJson(sSP.getString(LOAN_INFO, ""), LoanBean.class);
            return loanBean==null?new LoanBean():loanBean;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new LoanBean();
    }
}
