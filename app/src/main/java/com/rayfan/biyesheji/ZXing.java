package com.rayfan.biyesheji;

import android.app.Activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.text.Html;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rayfan.baidumap.api.BaiduMapUtil;
import com.rayfan.utils.Constant;
import com.google.zxing.activity.CaptureActivity;
import com.rayfan.utils.HtmlWithImage;
import com.rayfan.utils.ShowBigImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


public class ZXing extends Activity  implements View.OnClickListener{
    private Button btnQrCode; // 扫码
    private Button goThese;
    private TextView tvResult; // 结果
    private String scanResult = null;
    private JSONObject spotAllJson;
    private JSONObject saomiaoSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxing);

        initView();
        startQrCode();
    }

    private void initView() {
        btnQrCode = (Button) findViewById(R.id.btn_qrcode);
        btnQrCode.setOnClickListener(this);
        tvResult = (TextView) findViewById(R.id.txt_result);
        goThese = (Button) findViewById(R.id.saomiao_go_there);
        goThese.setOnClickListener(this);
        try{
            spotAllJson = new JSONObject(readSpotAll());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    // 开始扫码
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(ZXing.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(ZXing.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(ZXing.this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_qrcode:
                startQrCode();
                break;
            case R.id.saomiao_go_there:
                go_there();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            showIntroduce();
            //tvResult.setText(scanResult);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(ZXing.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
            case Constant.REQ_PERM_EXTERNAL_STORAGE:
                // 文件读写权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(ZXing.this, "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void go_there(){
        if(scanResult == null){
            Toast.makeText(ZXing.this, "请扫描一个景点", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            String destination = "华南师范大学" + saomiaoSpot.getString("name");
            String api = BaiduMapUtil.apiBaiduMapRoutePlan(ZXing.this, "", destination, "walking");
            Intent ll = new Intent();
            ll.setData(Uri.parse(api));
            startActivity(ll);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    private void showIntroduce(){
        String introduceFile = null;
        if(scanResult == null) return;
        try {
            JSONArray jsArr = spotAllJson.getJSONArray("教学科研机构");
            for(int i = 0; i < jsArr.length(); i++) {
                JSONObject jsobj = jsArr.getJSONObject(i);
                if(scanResult.equals(jsobj.getString("code"))){
                    saomiaoSpot = jsobj;
                    introduceFile = jsobj.getString("introduce");
                    break;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        HtmlWithImage.setContext(ZXing.this); // 必须设置
        HtmlWithImage.setShowBigImage(ShowBigImage.class); // 必须设置
        tvResult.setText(Html.fromHtml(HtmlWithImage.getHtmlString(introduceFile),
                new HtmlWithImage.AssetsImageGetter(tvResult), null));
        tvResult.setMovementMethod(HtmlWithImage.LinkMovementMethodExt.getInstance(HtmlWithImage.getShowBigImageHandler(), ImageSpan.class));


    }

    private String readSpotAll(){
        InputStream in = getResources().openRawResource(R.raw.scene_spot_all);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer stringBuffer = new StringBuffer();
        String str = null;
        try {
            while (null != (str = reader.readLine())){
                stringBuffer.append(str);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return  stringBuffer.toString();
    }


}
