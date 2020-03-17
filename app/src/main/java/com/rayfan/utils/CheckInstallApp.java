package com.rayfan.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import java.util.List;

public class CheckInstallApp {
    public static final String BaiduMapApp = "com.baidu.BaiduMap";

    public CheckInstallApp(){}

    public static boolean isInstalled(Context context, String packageName) {
        boolean installed = false;
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        List<ApplicationInfo> installedApplications = context.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo in : installedApplications) {
            if (packageName.equals(in.packageName)) {
                installed = true;
                break;
            } else {
                installed = false;
            }
        }
        return installed;
    }
}
