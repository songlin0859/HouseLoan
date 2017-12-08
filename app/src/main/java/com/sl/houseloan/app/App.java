package com.sl.houseloan.app;

import android.app.Application;

import com.sl.houseloan.util.SpUtil;

/**
 * Created by Administrator on 2017/12/8.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SpUtil.init(this);
    }
}
