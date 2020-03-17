package com.rayfan.biyesheji;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rayfan.utils.MyLog;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;


public class Home extends AppCompatActivity {
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;
    private ImageButton sceneSpotIntroduceButton = null, planPathButton = null, sceneMapButton = null, sceneQueryButton = null, facilityQueryButton = null;
    private ImageButton infoServiceButton = null, sceneScanButton = null, moreServiceButton = null;
    private TextView sceneActivityLabel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        sceneSpotIntroduceButton = (ImageButton) findViewById(R.id.home_button_sceneSpotIntroduce);
        planPathButton = (ImageButton) findViewById(R.id.home_button_planPath);
        sceneMapButton =(ImageButton) findViewById(R.id.home_button_sceneMap);
        sceneQueryButton = (ImageButton) findViewById(R.id.home_button_sceneQuery);
        facilityQueryButton = (ImageButton) findViewById(R.id.home_button_facilityQuery);
        infoServiceButton =(ImageButton) findViewById(R.id.home_button_infoService);
        sceneScanButton = (ImageButton) findViewById(R.id.home_button_sceneScan);
        moreServiceButton = (ImageButton) findViewById(R.id.home_button_moreService);
        sceneActivityLabel = (TextView) findViewById(R.id.home_label_sceneActivity);
        getPersimmions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.init_listener();
    }
    private void init_listener(){
        sceneSpotIntroduceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.d("sceneSpotIntroduce", "click");
                Intent intent = new Intent(Home.this, SceneSpotIntroduce.class);
                startActivity(intent);
            }
        });
        planPathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.d("planPath", "click");
                //Intent intent = new Intent(Home.this, PlanPath.class);
                Intent intent = new Intent(Home.this, DrivingRouteSearchDemo.class);
                startActivity(intent);
            }
        });
        sceneMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.d("sceneMap", "click");
//                Intent intent = new Intent(Home.this, SceneMap.class);
//                Intent intent = new Intent(Home.this, MainActivity.class);
                Intent intent = new Intent(Home.this, LocationTypeDemo.class);
                startActivity(intent);
            }
        });
        sceneQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.d("sceneQuery", "click");
                Intent intent = new Intent(Home.this, SceneQuery.class);
                startActivity(intent);
            }
        });
        facilityQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.d("facilityQuery", "click");
                Intent intent = new Intent(Home.this, FacilityQuery.class);
                startActivity(intent);
            }
        });
        infoServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.d("infoService", "click");
                Intent intent = new Intent(Home.this, InfoService.class);
                startActivity(intent);
            }
        });
        sceneScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.d("sceneScan", "click");
//                Intent intent = new Intent(Home.this, SceneScan.class);
                Intent intent = new Intent(Home.this, ZXing.class);
                startActivity(intent);
            }
        });
        moreServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.d("moreService", "click");
                //MyLog.d("id", "dfd"); getResources()
                Intent intent = new Intent(Home.this, MoreService.class);
                startActivity(intent);
            }
        });
        sceneActivityLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.d("sceneActivityLabel", "click");
                Intent intent = new Intent(Home.this, SceneActivity.class);
                startActivity(intent);
            }
        });
    }


    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }
            if(checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
            }



            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }
        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
