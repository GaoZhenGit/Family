package com.gz.family.application;

import android.app.Application;

import com.android.volley.toolbox.Volley;
import com.gz.family.Network.Net;
import com.gz.family.dataBase.DBhelper;

/**
 * Created by host on 2016/1/29.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Net.init(this);
        DBhelper.init(this);
    }
}
