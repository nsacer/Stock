package com.example.stock;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class ApplicationStock extends Application {

    private static ApplicationStock mApplication;

    public synchronized static ApplicationStock getInstance() {
        return mApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mApplication = this;
    }
}
