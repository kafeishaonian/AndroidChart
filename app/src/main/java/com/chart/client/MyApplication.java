package com.chart.client;

import android.app.Application;

/**
 * Created by Hongmingwei on 2018/3/28.
 * Email: 648600445@qq.com
 */

public class MyApplication extends Application {

    private static MyApplication instance = null;
    public MyApplication() {
        super();
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
