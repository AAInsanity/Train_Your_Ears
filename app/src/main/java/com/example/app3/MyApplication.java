package com.example.app3;

import android.app.Application;
import android.content.Context;

/**
 * Created by dengyitong on 6/24/17.
 */

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
