package com.blue.mediaplayer.base;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by xingyatong on 2018/7/2 11:03
 * Describe xxxxx
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //数据库初始化
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
