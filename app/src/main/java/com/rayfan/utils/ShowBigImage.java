package com.rayfan.utils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.rayfan.biyesheji.R;
import com.rayfan.utils.MyLog;


public class ShowBigImage extends Activity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.d("showbigiamge", "进入show big image");
        setContentView(R.layout.simple);
        textView = (TextView) findViewById(R.id.simpleText);
        textView.setText("show big image");

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
