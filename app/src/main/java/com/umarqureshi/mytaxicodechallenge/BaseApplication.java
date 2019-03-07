package com.umarqureshi.mytaxicodechallenge;

import android.app.Application;

public class BaseApplication extends Application {

    public static String PACKAGE_NAME;

    @Override
    public void onCreate() {
        super.onCreate();

        PACKAGE_NAME = getApplicationContext().getPackageName();
    }
}
