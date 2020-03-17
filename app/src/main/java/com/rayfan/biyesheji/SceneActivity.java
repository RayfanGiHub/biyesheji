package com.rayfan.biyesheji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;



public class SceneActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 暂时不做处理
        Intent intent = new Intent(SceneActivity.this, InfoService.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
