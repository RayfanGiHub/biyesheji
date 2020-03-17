package com.rayfan.biyesheji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.rayfan.utils.HtmlWithImage;
import com.rayfan.utils.MyLog;
import com.rayfan.utils.ShowBigImage;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class SceneSpotModel extends Activity {
    private TextView title;
    private TextView introduce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_spot_model);
        title = (TextView) findViewById(R.id.scene_model_title);
        introduce = (TextView) findViewById(R.id.scene_model_introduce);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String data = null;
        try{
            data = intent.getStringExtra("data");
        }catch (Exception e){

        }
        MyLog.d("modelData", data);
        String introduceFile = null;
        try{
            JSONObject obj = new JSONObject(data);
            introduceFile = obj.getString("introduce");
            title.setText(obj.getString("name"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        HtmlWithImage.setContext(SceneSpotModel.this); // 必须设置
        HtmlWithImage.setShowBigImage(ShowBigImage.class); // 必须设置
        introduce.setText(Html.fromHtml(HtmlWithImage.getHtmlString(introduceFile),
                new HtmlWithImage.AssetsImageGetter(introduce), null));
        introduce.setMovementMethod(HtmlWithImage.LinkMovementMethodExt.getInstance(HtmlWithImage.getShowBigImageHandler(), ImageSpan.class));


    }
}
