package com.rayfan.biyesheji;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.location.service.LocationService;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

public class LocationApplication extends Application {
    public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
