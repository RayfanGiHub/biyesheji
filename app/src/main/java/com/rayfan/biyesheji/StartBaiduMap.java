package com.rayfan.biyesheji;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.rayfan.baidumap.api.BaiduMapUtil;
import com.rayfan.utils.CheckInstallApp;
import com.rayfan.utils.MyLog;

import java.util.List;


public class StartBaiduMap extends Activity {
    private static String origin = "华南师范大学大学城校区食堂"; // 默认值是当前定位地址
    private static String destination = "华南师范大学大学城校区图书馆";
    private static String coord_type = BaiduMapUtil.COORD_TYPE.bd09ll.toString(); // 百度经纬度坐标
    private static String mode = BaiduMapUtil.MODE.walking.toString();
    private static String apiURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CheckInstallApp.isInstalled(getApplication(), CheckInstallApp.BaiduMapApp)){
            apiURL = BaiduMapUtil.apiBaiduMapRoutePlan(getApplicationContext(), "华南师范大学大学城校区图书馆","华南师范大学大学城校区食堂","walking");
            this.startBaiduMap(apiURL);
        }
    }
    private void startBaiduMap(String api){
        if(api.length() == 0) return;
        Intent i1 = new Intent();
        i1.setData(Uri.parse(api));
        startActivity(i1);
    }

}
