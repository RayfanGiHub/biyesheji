package com.rayfan.biyesheji;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.rayfan.utils.ImageTitleIntroductionListAdapterUtil;
import com.rayfan.utils.ImageTitleIntroductionListAdapterUtil.Item;
import com.rayfan.utils.MyLog;


public class FacilityQuery extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facillity_query);
        ListView mListView = (ListView) findViewById(R.id.listView);
        final ImageTitleIntroductionListAdapterUtil listAdapterUtil = new ImageTitleIntroductionListAdapterUtil();
        listAdapterUtil.setContext(FacilityQuery.this);
        Item item_1 = new Item(
                R.drawable.scnu_icon, R.string.facility_query_rent_classroom,R.string.facility_query_rent_classroom_introduction, null);
        Item item_2 = new Item(
                R.drawable.scnu_icon, R.string.facility_query_mass_organization_query, R.string.facility_query_mass_organization_query_introduction, null);
        Item item_3 = new Item(
                R.drawable.scnu_icon, R.string.facility_query_stadium,R.string.facility_query_stadium_introduction, null);
        Item item_4 = new Item(
                R.drawable.scnu_icon, R.string.facility_query_canteen, R.string.facility_query_canteen_introduction, null);
        listAdapterUtil.appendItem(item_1);
        listAdapterUtil.appendItem(item_2);
        listAdapterUtil.appendItem(item_3);
        listAdapterUtil.appendItem(item_4);
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
