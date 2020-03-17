package com.rayfan.biyesheji;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.rayfan.biyesheji.infoservice.TaoYuan;
import com.rayfan.biyesheji.infoservice.SCNUAtivity;
import com.rayfan.utils.ImageTitleIntroductionListAdapterUtil;
import com.rayfan.utils.ImageTitleIntroductionListAdapterUtil.Item;
import com.rayfan.utils.ImageTitleIntroductionListAdapterUtil.ItemListAdapter;
import com.rayfan.utils.MyLog;


public class InfoService extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoservice);
        ListView mListView = (ListView) findViewById(R.id.listView);
        final ImageTitleIntroductionListAdapterUtil listAdapterUtil = new ImageTitleIntroductionListAdapterUtil();
        listAdapterUtil.setContext(InfoService.this);
        Item item_1 = new Item(
                R.drawable.scnu_icon, R.string.infoservice_taoyuan,R.string.infoservice_taoyuan_introduction, TaoYuan.class);
        Item item_2 = new Item(
                R.drawable.scnu_icon, R.string.infoservice_activity_dushu, R.string.infoservice_activity_dushu_introduction, SCNUAtivity.class);
        listAdapterUtil.appendItem(item_1);
        listAdapterUtil.appendItem(item_2);
        final ItemListAdapter itemListAdapter = listAdapterUtil.getItemListAdapter();
        mListView.setAdapter(itemListAdapter);
        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(view != null){
                    listAdapterUtil.onListItemClick(position);
                    itemListAdapter.notifyDataSetChanged(); // 通知重绘页面
                }else{
                    MyLog.d("参数错误", listAdapterUtil.toString());
                }

            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
    }


}
