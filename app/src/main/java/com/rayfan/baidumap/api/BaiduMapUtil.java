package com.rayfan.baidumap.api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.baidu.location.PoiRegion;
import com.baidu.location.service.LocationService;
import com.rayfan.utils.MyLog;

import org.apache.log4j.spi.LocationInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class BaiduMapUtil {
    private static String origin = ""; // 默认值是当前定位地址
    private static String destination = "华南师范大学大学城校区图书馆";
    private static String coord_type = COORD_TYPE.bd09ll.toString(); // 百度经纬度坐标
    private static String mode = MODE.walking.toString();
    private static String region = "广州市";
    private static String origin_region = "广州市";
    private static String destination_region = "广州市";
    private static int sy = 0;
    private static int index = 0;
    private static int target = 0;
    private static String car_type = CAR_TYPE.BLK.toString();
    private static String viaPoints = ""; // 最多只能经过三个点 且要是json格式
    private static String src = "andr.rayfan.biyesheji";
    private static String center = ""; // 显示地图的中心点 先纬度 再经度 lat,lng (先纬度，后经度)
    private static String bounds = ""; // 显示地图的范围， 先左下角 后又下角 lat,lng,lat,lng (先纬度，后经度, 先左下,后右上)
    private static String traffic = "on"; // on 开启路况 off 关闭
    private static int zoom = 16; // 地图展示的级别 zoom 越大 比例尺越小
    private static String location = ""; // at,lng (先纬度，后经度)
    private static String title = "rayfan"; // 打点标题
    private static String content = ""; // 打点内容
    private static String query = "美食";
    private static int radius = 5000; // 检索半径,单位:m

    private static LocationService locationService;
    private static String locationInfo = "";



    public static String apiBaiduMapShowMap(Context context, String center, String bounds) {
        return originBaiduMapShowMap(context, center, bounds, coord_type, traffic, zoom, src);
    }
    public static String apiBaiduMapShowMap(Context context, String center, String bounds, int zoom) {
        return originBaiduMapShowMap(context, center, bounds, coord_type, traffic, zoom, src);
    }



    public static String apiBaiduMapMarkPointBySelf(Context context, String location, String title){
        return originBaiduMapMarkPointBySelf(context, location, title, content, coord_type, traffic, src);
    }
    public static String apiBaiduMapShowMapDetial(Context context){
        return originBaiduMapShowMapDetial(context, src);
    }
    public static String apiBaiduMapAnalyseAddress(Context context, String address){
        return originBaiduMapAnalyseAddress(context, address, src);
    }
    public static String apiBaiduMapQueryByKey(Context context, String query){
        return originBaiduMapQueryByKey(context, query, region, location, bounds, coord_type, radius, src);
    }
    public static String apiBaiduMapRoutePlan(Context context, String origin, String _destination, String _mode) {

        if(TextUtils.isEmpty(_destination)){
            Toast.makeText(context, "没有终点，将导航到默认地点:" + destination, Toast.LENGTH_SHORT).show();
            return "";
        }
        if(TextUtils.isEmpty(_mode)) _mode = mode;
        String apiURL = originBaiduMapRoutePlan(context, origin, _destination, coord_type, _mode, region, origin_region, destination_region,
                sy, index, target, car_type, viaPoints, src);
        return apiURL;
    }

    public static String apiBaiduMapRoutePlan(Context context, String origin, String _destination, String _mode, String viaPoints) {
        /**
         * viaPoints 是中间要经过的点
         */
        if(TextUtils.isEmpty(_destination)){
            Toast.makeText(context, "没有终点，将导航到默认地点:" + destination, Toast.LENGTH_SHORT).show();
            return "";
        }
        if(TextUtils.isEmpty(_mode)) _mode = mode;
        String apiURL = originBaiduMapRoutePlan(context, origin, _destination, coord_type, _mode, region, origin_region, destination_region,
                sy, index, target, car_type, viaPoints, src);
        return apiURL;
    }

    public static String apiBaiduMapQueryLine(Context context, String region, String name){
        return originBaiduMapQueryLine(context, region, name, src);
    }
    public static String apiBaiduMapQueryNearby(Context context, String query){
        return originBaiduMapQueryNearby(context, center, query, coord_type, radius, src);
    }
    public static String apiBaiduMapBikeNavi(Context context,String origin, String destination){
        return originBaiduMapBikenavi(context, origin, coord_type, destination, src);
    }



    // 2.2.1 展示地图
    public static String originBaiduMapShowMap(Context context, String center, String bounds, String coord_type, String traffic, int zoom, String src){
        // "baidumap://map/show?center=40.057406655722,116.29644071728&zoom=11
        // &traffic=on&bounds=37.8608310000,112.5963090000,42.1942670000,118.9491260000&src=andr.baidu.openAPIdemo"
        if(TextUtils.isEmpty(center) || TextUtils.isEmpty(bounds) || TextUtils.isEmpty(coord_type) || TextUtils.isEmpty(traffic) ||
                TextUtils.isEmpty(src)){
            return "";
        }
        StringBuffer URL = new StringBuffer("baidumap://map/show?");
        URL.append("center=" + center);
        URL.append("&bounds" + bounds);
        URL.append("&traffic=" + traffic);
        URL.append("&zoom=" + zoom);
        URL.append("&src=" + src);
        MyLog.d("show map api", URL.toString());
        return URL.toString();
    }

    // 2.2.2 自定义打点
    public static String originBaiduMapMarkPointBySelf(Context context, String location, String title, String content, String coord_type,
                                                       String traffic, String src){
        // "baidumap://map/marker?location=40.057406655722,116.2964407172&title=Marker&content=makeamarker&traffic=on&src=andr.baidu.openAPIdemo"
        if(TextUtils.isEmpty(location) || TextUtils.isEmpty(title) || TextUtils.isEmpty(coord_type) ||
                TextUtils.isEmpty(traffic) || TextUtils.isEmpty(src)){
            return "";
        }
        StringBuffer URL = new StringBuffer("baidumap://map/marker?");
        URL.append("location=" + location);
        URL.append("&title=" + title);
        if(!TextUtils.isEmpty(content)){
            URL.append("&content=" + content);
        }
        URL.append("&coord_type" + coord_type);
        URL.append("&traffic=" + traffic);
        URL.append("&src=" + src);
        MyLog.d("mark point by self api", URL.toString());
        return URL.toString();
    }

    // 2.2.3 展示地图图区
    public static String originBaiduMapShowMapDetial(Context context, String src){
       // baidumap://map?
        if(TextUtils.isEmpty(src)) return "";
        StringBuffer URL = new StringBuffer("baidumap://map?");
        URL.append("src=" + src);
        MyLog.d("show map detial api", URL.toString());
        return URL.toString();
    }

    // 2.2.4 地址解析
    public static String originBaiduMapAnalyseAddress(Context context, String address, String src){
        // "baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address=北京市海淀区上地信息路9号奎科科技大厦"
        StringBuffer url = new StringBuffer("baidumap://map/geocoder?");
        if(TextUtils.isEmpty(address) || TextUtils.isEmpty(src)){
            return "";
        }
        url.append("address=" + address);
        url.append("&src=" + src);
        MyLog.d("analyse address api", url.toString());
        return url.toString();
    }
    // 2.3.1 POI搜索 根据给定的关键字、检索条件进行检索
    public static String originBaiduMapQueryByKey(Context context, String query, String region, String location, String bounds, String coord_type, int radius, String src){
        if(TextUtils.isEmpty(query) || TextUtils.isEmpty(coord_type) || TextUtils.isEmpty(src)){
            return "";
        }
        // "baidumap://map/place/search?query=美食&region=beijing&location=39.915168,116.403875&radius=1000&
        // bounds=37.8608310000,112.5963090000,42.1942670000,118.9491260000&src=andr.baidu.openAPIdemo"
        if(TextUtils.isEmpty(query)) return "";
        StringBuffer url = new StringBuffer("baidumap://map/place/search?");
        url.append("query=" + query);
        if(!TextUtils.isEmpty(region)){
            url.append("&region=" + region);
        }
        if(!TextUtils.isEmpty(location)){
            url.append("&location=" + location);
        }
        if(!TextUtils.isEmpty(bounds)){
            url.append("&bounds=" + bounds);
        }
        if(!TextUtils.isEmpty(coord_type)){
            url.append("&coord_type=" + coord_type);
        }
        url.append("&radius" + radius);
        if(!TextUtils.isEmpty(src)){
            url.append("&src=" + src);        }

        MyLog.d("query by key api", url.toString());
        return url.toString();
    }
    // 2.3.2 路线规划
    public static String originBaiduMapRoutePlan(Context context, String origin, String destination, String coord_type, String mode
            , String region, String origin_region, String destination_region
            , int sy, int index, int target, String car_type, String viaPoints, String src) {

        StringBuffer sb = new StringBuffer("baidumap://map/direction?");
        boolean isHasdestination = false;
        if (!TextUtils.isEmpty(destination)) {
            isHasdestination = true;
            sb.append("destination=" + destination);
        }
        if (!TextUtils.isEmpty(origin)) {
            sb.append("&origin=" + origin);
        }

        if (!isHasdestination) {
            Toast.makeText(context, "请输入终点", Toast.LENGTH_SHORT).show();
            return "";
        }
        if (!TextUtils.isEmpty(coord_type)) {
            sb.append("&coord_type=" + coord_type);
        }
        if (!TextUtils.isEmpty(mode)) {
            sb.append("&mode=" + mode);
        }
        if (!TextUtils.isEmpty(region)) {
            sb.append("&region=" + region);
        }
        if (!TextUtils.isEmpty(origin_region)) {
            sb.append("&origin_region=" + origin_region);
        }
        if (!TextUtils.isEmpty(destination_region)) {
            sb.append("&destination_region=" + destination_region);
        }
        if("transit".equals(mode)){
            sb.append("&sy=" + sy);
            sb.append("&index=" + index);
            sb.append("&target=" + target);
        }

        if ("driving".equals(mode) && !TextUtils.isEmpty(car_type)) {
            sb.append("&car_type=" + car_type);
        }
        if (!TextUtils.isEmpty(viaPoints)) {
            sb.append("&viaPoints=" + viaPoints);
        }
        if (!TextUtils.isEmpty(src)) {
            sb.append("&src=" + src);
        }
        MyLog.d("路线规划 request baidu map url", sb.toString());
        return sb.toString();
//        Intent i1 = new Intent();
////            i1.setData(Uri.parse(sb.toString()));
//        i1.setData(Uri.parse("baidumap://map/direction?origin=name:对外经贸大学|latlng:39.98871,116.43234&destination=西直门&coord_type=bd09ll&mode=transit&sy=3&index=0&target=1&src=andr.baidu.openAPIdemo"));
//        //          baidumap://map/direction?origin=华南师范大学大学城校区图书馆&destination=华南师范大学大学城校区食堂&coord_type=bd09ll&mode=walking&region=广州市&origin_region=广州市&destination_region=广州市&src=andr.rayfan.biyesheji
//        context.startActivity(i1);

    }

    // 2.3.3 公交、地铁线路查询
    public static String originBaiduMapQueryLine(Context context, String region, String name, String src){
        if(TextUtils.isEmpty(region) || TextUtils.isEmpty(name) || TextUtils.isEmpty(src)){
            return "";
        }
        StringBuffer url = new StringBuffer("baidumap://map/line?");
        url.append("region=" + region);
        url.append("&name=" + name);
        url.append("&src="+ src);
        MyLog.d("query line api", url.toString());
        return url.toString();
    }

    // 2.3.4 附近搜索
    public static String originBaiduMapQueryNearby(Context context, String center, String query, String coord_type, int radius, String src){
        if(TextUtils.isEmpty(src)){
            return "";
        }
        StringBuffer url = new StringBuffer("baidumap://map/place/nearby?");
        url.append("src=" + src);
        if(!TextUtils.isEmpty(center)){
            url.append("&center=" + center);
        }
        if(!TextUtils.isEmpty(query)){
            url.append("&query=" + query);
        }
        if(!TextUtils.isEmpty(coord_type)){
            url.append("&coord_type=" + coord_type);
        }
        if(radius > 100){
            url.append("&radius=" + radius);
        }
        MyLog.d("query nearby api", url.toString());
        return url.toString();
    }

    // 2.4.2 骑行导航
    public static String originBaiduMapBikenavi(Context context, String origin, String coord_type, String destination, String src){
        if(TextUtils.isEmpty(origin) || TextUtils.isEmpty(coord_type) || TextUtils.isEmpty(destination) || TextUtils.isEmpty(src)){
            return "";
        }
        StringBuffer url = new StringBuffer("baidumap://map/bikenavi?");
        url.append("origin="+ origin);
        url.append("&coord_type=" + coord_type);
        url.append("&destination=" + destination);
        url.append("&src=" + src);
        MyLog.d("baidu map bikenavi api", url.toString());
        return url.toString();
    }

    // 2.4.3 步行导航
    public static String originBaiduMapWalknavi(Context context, String origin, String coord_type, String destination, String mode, String src){
        /**
         * origin destination 是经纬度：39.98871,116.43234
         */
        if(TextUtils.isEmpty(origin) || TextUtils.isEmpty(coord_type) || TextUtils.isEmpty(destination)) return "";
        StringBuffer url = new StringBuffer("baidumap://map/walknavi");
        url.append("origin=" + origin);
        url.append("&coord_type=" + coord_type);
        url.append("&destination=" + destination);
        url.append("&mode=" + mode);
        url.append("&src=" + src);
        MyLog.d("baidu map walk navi api", url.toString());
        return url.toString();
    }






    public static void setViaPoints(List<String> points){
        /**
         * {"viaPoints": [{"name": "北京西站"},{"name": "北京动物园"},{"name": "清华大学"}]}
         */
        JSONObject viaPointsJson = new JSONObject();
        JSONArray pointsArr = new JSONArray();
        try {
            viaPointsJson.put("viaPoints", pointsArr);
            for (String str: points) {
                pointsArr.put(new JSONObject().put("name",str));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viaPoints = viaPointsJson.toString();
    }



    public enum COORD_TYPE {
        bd09ll, bd09mc, gcj02, wgs84
    }

    public enum MODE {
        driving, walking, riding, transit
    }

    public enum CAR_TYPE {
        BLK, TIME, DIS, FEE, HIGHWAY, DEFAULT
    }

    /**
     * locationService = ((LocationApplication) getApplication()).locationService;
     *         BaiduMapUtil.setLocationService(locationService);
     *         locationService.registerListener(rayfanListener);
     *         info = BaiduMapUtil.getLocationInfo();
     *                     MyLog.d("location info", info);
     *
     * @param _locationService
     */
    public static void setLocationService(LocationService _locationService){
        locationService = _locationService;
    }
    public static class LocationListener extends BDAbstractLocationListener {

        /**
         * 定位请求回调函数
         * @param location 定位结果
         */
        @Override
        public void onReceiveLocation(BDLocation location) {

            try{
                // TODO Auto-generated method stub
                if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                    int tag = 1;
                    JSONObject locationInfoJson = new JSONObject();
                    StringBuffer sb = new StringBuffer(256);
                    sb.append("time : ");
                    /**
                     * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                     * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                     */
                    sb.append(location.getTime());
                    locationInfoJson.put("time",location.getTime());
                    sb.append("\nlocType : ");// 定位类型
                    sb.append(location.getLocType());
                    locationInfoJson.put("locType", location.getLocType());
                    sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                    sb.append(location.getLocTypeDescription());
                    locationInfoJson.put("locType description", location.getLocationDescribe());
                    sb.append("\nlatitude : ");// 纬度
                    sb.append(location.getLatitude());
                    locationInfoJson.put("latitude", location.getLatitude());
                    sb.append("\nlongtitude : ");// 经度
                    sb.append(location.getLongitude());
                    locationInfoJson.put("longtitude", location.getLongitude());
                    sb.append("\nradius : ");// 半径
                    sb.append(location.getRadius());
                    locationInfoJson.put("radius", location.getRadius());
                    sb.append("\nCountryCode : ");// 国家码
                    sb.append(location.getCountryCode());
                    locationInfoJson.put("countryCode", location.getCountryCode());
                    sb.append("\nProvince : ");// 获取省份
                    sb.append(location.getProvince());
                    locationInfoJson.put("province", location.getProvince());
                    sb.append("\nCountry : ");// 国家名称
                    sb.append(location.getCountry());
                    locationInfoJson.put("country", location.getCountry());
                    sb.append("\ncitycode : ");// 城市编码
                    sb.append(location.getCityCode());
                    locationInfoJson.put("cityCode", location.getCityCode());
                    sb.append("\ncity : ");// 城市
                    sb.append(location.getCity());
                    locationInfoJson.put("city", location.getCity());
                    sb.append("\nDistrict : ");// 区
                    sb.append(location.getDistrict());
                    locationInfoJson.put("district", location.getDistrict());
                    sb.append("\nTown : ");// 获取镇信息
                    sb.append(location.getTown());
                    locationInfoJson.put("town", location.getTown());
                    sb.append("\nStreet : ");// 街道
                    sb.append(location.getStreet());
                    locationInfoJson.put("street", location.getStreet());
                    sb.append("\naddr : ");// 地址信息
                    sb.append(location.getAddrStr());
                    locationInfoJson.put("addr", location.getAddrStr());
                    sb.append("\nStreetNumber : ");// 获取街道号码
                    sb.append(location.getStreetNumber());
                    locationInfoJson.put("streetNumber", location.getStreetNumber());
                    sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                    sb.append(location.getUserIndoorState());
                    locationInfoJson.put("userIndoorState", location.getUserIndoorState());
                    sb.append("\nDirection(not all devices have value): ");
                    sb.append(location.getDirection());// 方向
                    locationInfoJson.put("direction", location.getDirection());
                    sb.append("\nlocationdescribe: ");
                    sb.append(location.getLocationDescribe());// 位置语义化信息
                    locationInfoJson.put("locationdescribe", location.getLocationDescribe());
                    JSONArray poiArr = new JSONArray();
                    locationInfoJson.put("pio", poiArr);
                    sb.append("\nPoi: ");// POI信息
                    if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                        for (int i = 0; i < location.getPoiList().size(); i++) {
                            Poi poi = (Poi) location.getPoiList().get(i);
                            sb.append("poiName:");
                            sb.append(poi.getName() + ", ");
                            sb.append("poiTag:");
                            sb.append(poi.getTags() + "\n");
                            JSONObject jsobj = new JSONObject();
                            jsobj.put("poiName", poi.getName());
                            jsobj.put("poiTag", poi.getName());
                            poiArr.put(jsobj);
                        }
                    }
                    if (location.getPoiRegion() != null) {
                        sb.append("PoiRegion: ");// 返回定位位置相对poi的位置关系，仅在开发者设置需要POI信息时才会返回，在网络不通或无法获取时有可能返回null
                        PoiRegion poiRegion = location.getPoiRegion();
                        sb.append("DerectionDesc:"); // 获取POIREGION的位置关系，ex:"内"
                        sb.append(poiRegion.getDerectionDesc() + "; ");
                        sb.append("Name:"); // 获取POIREGION的名字字符串
                        sb.append(poiRegion.getName() + "; ");
                        sb.append("Tags:"); // 获取POIREGION的类型
                        sb.append(poiRegion.getTags() + "; ");
                    }
                    sb.append("\nSDK版本: ");
                    sb.append(locationService.getSDKVersion()); // 获取SDK版本
                    locationInfoJson.put("SDKVersion", locationService.getSDKVersion());
                    if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                        sb.append("\nspeed : ");
                        sb.append(location.getSpeed());// 速度 单位：km/h
                        sb.append("\nsatellite : ");
                        sb.append(location.getSatelliteNumber());// 卫星数目
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 海拔高度 单位：米
                        sb.append("\ngps status : ");
                        sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                        sb.append("\ndescribe : ");
                        sb.append("gps定位成功");
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                        // 运营商信息
                        if (location.hasAltitude()) {// *****如果有海拔高度*****
                            sb.append("\nheight : ");
                            sb.append(location.getAltitude());// 单位：米
                        }
                        sb.append("\noperationers : ");// 运营商信息
                        sb.append(location.getOperators());
                        sb.append("\ndescribe : ");
                        sb.append("网络定位成功");
                        locationInfoJson.put("describe", "网络定位成功");
                    } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                        sb.append("\ndescribe : ");
                        sb.append("离线定位成功，离线定位结果也是有效的");
                    } else if (location.getLocType() == BDLocation.TypeServerError) {
                        sb.append("\ndescribe : ");
                        sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                        sb.append("\ndescribe : ");
                        sb.append("网络不同导致定位失败，请检查网络是否通畅");
                    } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                        sb.append("\ndescribe : ");
                        sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    }
                    MyLog.d("location info json", locationInfoJson.toString());
                    //MyLog.d("location info", sb.toString());
                    //logMsg(sb.toString(), tag);
                    logMsg(locationInfoJson.toString(), tag);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            super.onConnectHotSpotMessage(s, i);
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * @param locType 当前定位类型
         * @param diagnosticType 诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        @Override
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
            super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
            int tag = 2;
            StringBuffer sb = new StringBuffer(256);
            sb.append("诊断结果: ");
            if (locType == BDLocation.TypeNetWorkLocation) {
                if (diagnosticType == 1) {
                    sb.append("网络定位成功，没有开启GPS，建议打开GPS会更好");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 2) {
                    sb.append("网络定位成功，没有开启Wi-Fi，建议打开Wi-Fi会更好");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeOffLineLocationFail) {
                if (diagnosticType == 3) {
                    sb.append("定位失败，请您检查您的网络状态");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeCriteriaException) {
                if (diagnosticType == 4) {
                    sb.append("定位失败，无法获取任何有效定位依据");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 5) {
                    sb.append("定位失败，无法获取有效定位依据，请检查运营商网络或者Wi-Fi网络是否正常开启，尝试重新请求定位");
                    sb.append(diagnosticMessage);
                } else if (diagnosticType == 6) {
                    sb.append("定位失败，无法获取有效定位依据，请尝试插入一张sim卡或打开Wi-Fi重试");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 7) {
                    sb.append("定位失败，飞行模式下无法获取有效定位依据，请关闭飞行模式重试");
                    sb.append("\n" + diagnosticMessage);
                } else if (diagnosticType == 9) {
                    sb.append("定位失败，无法获取任何有效定位依据");
                    sb.append("\n" + diagnosticMessage);
                }
            } else if (locType == BDLocation.TypeServerError) {
                if (diagnosticType == 8) {
                    sb.append("定位失败，请确认您定位的开关打开状态，是否赋予APP定位权限");
                    sb.append("\n" + diagnosticMessage);
                }
            }
            //logMsg(sb.toString(), tag); // 定位失败
            logMsg("", tag);
        }
    }

    private static void logMsg(String toString, int tag) {
        locationInfo = toString;
    }
    public static String getLocationInfo(){
        return locationInfo;
    }


}