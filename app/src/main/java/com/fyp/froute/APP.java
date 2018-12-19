package com.fyp.froute;

import android.app.Application;

import com.fyp.routeapi.RouteUtils;

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RouteUtils routeUtils = RouteUtils.getInstance(this);
        routeUtils.setDebug(true);
        routeUtils.init();
    }
}
