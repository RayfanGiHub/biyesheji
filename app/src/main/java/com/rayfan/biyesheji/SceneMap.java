package com.rayfan.biyesheji;

import android.os.Bundle;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import androidx.appcompat.app.AppCompatActivity;


public class SceneMap extends AppCompatActivity {
    private MapView mMapView = null;
    private BDLocation location = null;
    private BaiduMap mBaiduMap = null;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.map);
        mMapView = (MapView) findViewById(R.id.map);

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        System.out.println("on resume");

        //mLocationClient.stop();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
    private void operateBaiduMap() {

        mBaiduMap.setTrafficEnabled(true); // 开启交通示图
        mBaiduMap.setMyLocationEnabled(true); // 开始定位图层
        //定位初始化
        mLocationClient = new LocationClient(this);
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);

        /**
         * 通过MyLocationConfiguration类来构造包括定位的属性，
         * 定位模式、是否开启方向、设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色5个属性。
         * MyLocationConfiguration(LocationMode mode,
         *                         boolean enableDirection,
         *                         BitmapDescriptor customMarker,
         *                         int accuracyCircleFillColor,
         *                         int accuracyCircleStrokeColor)
         */

        //开启地图定位图层
        mLocationClient.start();




    }

    // 通过继承抽象类BDAbstractListener并重写其onReceieveLocation方法来获取定位数据，并将其传给MapView
//    public class MyLocationListener extends BDAbstractLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            //mapView 销毁后不在处理新接收的位置
//            if (location == null || mMapView == null){
//                return;
//            }
//            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(location.getDirection()).latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//            mBaiduMap.setMyLocationData(locData);
//        }
//    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            System.out.println("经纬度信息latitude " + latitude  + "longitude  " + longitude + "radius " + radius);

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        }
    }


}
