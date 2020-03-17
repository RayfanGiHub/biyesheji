package com.rayfan.biyesheji;


import android.os.Bundle;
import android.text.Html;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.rayfan.utils.HtmlWithImage;
import com.rayfan.utils.ShowBigImage;

import androidx.appcompat.app.AppCompatActivity;


public class SceneSpotIntroduce extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction);
        TextView XueXiaoIntroductionTextView = (TextView) findViewById(R.id.XueXiaoIntroductionTextView);
        HtmlWithImage.setContext(SceneSpotIntroduce.this); // 必须设置
        HtmlWithImage.setShowBigImage(ShowBigImage.class); // 必须设置
        XueXiaoIntroductionTextView.setText(Html.fromHtml(HtmlWithImage.getHtmlString("xuexiao_introduction.txt"),
                new HtmlWithImage.AssetsImageGetter(XueXiaoIntroductionTextView), null));
        XueXiaoIntroductionTextView.setMovementMethod(HtmlWithImage.LinkMovementMethodExt.getInstance(HtmlWithImage.getShowBigImageHandler(), ImageSpan.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }




}

