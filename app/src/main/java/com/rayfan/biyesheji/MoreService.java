package com.rayfan.biyesheji;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rayfan.utils.ImageTitleIntroductionListAdapterUtil;
import com.rayfan.utils.MyLog;


public class MoreService extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moreservice);
        ListView mListView = (ListView) findViewById(R.id.listView);
        final ImageTitleIntroductionListAdapterUtil listAdapterUtil = new ImageTitleIntroductionListAdapterUtil();
        listAdapterUtil.setContext(MoreService.this);
        ImageTitleIntroductionListAdapterUtil.Item item_1 = new ImageTitleIntroductionListAdapterUtil.Item(
                R.drawable.scnu_icon, R.string.more_service_about,R.string.more_service_about_introduction, null);
        ImageTitleIntroductionListAdapterUtil.Item item_2 = new ImageTitleIntroductionListAdapterUtil.Item(
                R.drawable.scnu_icon, R.string.more_service_Tel, R.string.more_service_Tel_introduction, null);
        listAdapterUtil.appendItem(item_1);
        listAdapterUtil.appendItem(item_2);
        mListView.setAdapter(listAdapterUtil.getItemListAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(view != null){
                    listAdapterUtil.onListItemClick(position);
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
