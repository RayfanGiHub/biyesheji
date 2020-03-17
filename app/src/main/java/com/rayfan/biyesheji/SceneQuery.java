package com.rayfan.biyesheji;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rayfan.baidumap.api.BaiduMapUtil;
import com.rayfan.utils.ImageTitleIntroductionListAdapterUtil;
import com.rayfan.utils.ImageTitleIntroductionListAdapterUtil.Item;
import com.rayfan.utils.ImageTitleIntroductionListAdapterUtil.ItemListAdapter;
import com.rayfan.utils.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SceneQuery extends Activity {
    private Button queryButton;
    private EditText queryInput;
    private LinearLayout frameSwitch;
    private JSONObject spotAllJson;
    private Button view_all;
    private Button goThere;
    private ArrayList<Item> spotAllListViewItems = new ArrayList<Item>();
    private ArrayList<Item> clickedSpotItems = new ArrayList<Item>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scene_query);
        // init
        try{
            spotAllJson = new JSONObject(readSpotAll());
        }catch (JSONException e){
            e.printStackTrace();
        }
        getSpotItems();
        queryInput = (EditText) findViewById(R.id.inputPlace);
        queryButton = (Button) findViewById(R.id.beginQuery);
        frameSwitch = (LinearLayout) findViewById(R.id.frameSwitch);
        view_all = (Button) findViewById(R.id.view_scene_all);
        goThere = (Button) findViewById(R.id.scene_go_thesePlace);
        queryALL();
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryClicked();
            }
        });
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryALL();
            }
        });
        goThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTherePlace();
            }
        });


    }

    private void queryALL(){
        View allView = getLayoutInflater().inflate(R.layout.scene_query_all, null);
        ListView spotAllListView = (ListView) allView.findViewById(R.id.scene_all);
        final ImageTitleIntroductionListAdapterUtil listAdapterUtil = new ImageTitleIntroductionListAdapterUtil();
        listAdapterUtil.setContext(SceneQuery.this);
        listAdapterUtil.setItems(spotAllListViewItems);
        final ItemListAdapter itemListAdapter = listAdapterUtil.getItemListAdapter();
        spotAllListView.setAdapter(itemListAdapter);
        spotAllListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(view != null){
                    listAdapterUtil.onListItemClick(position, spotAllJson.toString());
                    itemListAdapter.notifyDataSetChanged(); // 通知重绘页面
                }else{
                    MyLog.d("参数错误", listAdapterUtil.toString());
                }

            }
        });
        frameSwitch.removeAllViews();
        frameSwitch.addView(allView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }
    private void queryClicked(){
        View clickedView = getLayoutInflater().inflate(R.layout.scene_query_clicked, null);
        ListView queryListView = (ListView) clickedView.findViewById(R.id.scene_clicked);
        final ImageTitleIntroductionListAdapterUtil listAdapterUtil = new ImageTitleIntroductionListAdapterUtil();
        listAdapterUtil.setContext(SceneQuery.this);
        isClickedItem();
        matchQuery();
        listAdapterUtil.setItems(clickedSpotItems);
        final ItemListAdapter itemListAdapter = listAdapterUtil.getItemListAdapter();
        queryListView.setAdapter(itemListAdapter);
        queryListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(view != null){
                    listAdapterUtil.onListItemClick(position, spotAllJson.toString());
                    itemListAdapter.notifyDataSetChanged(); // 通知重绘页面
                }else{
                    MyLog.d("参数错误", listAdapterUtil.toString());
                }

            }
        });
        frameSwitch.removeAllViews();
        frameSwitch.addView(clickedView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }
    private void isClickedItem(){
        clickedSpotItems.clear();
        for(Item item: spotAllListViewItems){
            if(item.isClicked()){
                clickedSpotItems.add(item);
            }
        }
    }
    private void matchQuery(){
        // 通过正则匹配
        ArrayList<Item> matchItemIndex = new ArrayList<Item>();
        String pattern = queryInput.getText().toString();
        for(Item item: spotAllListViewItems){
            boolean isMathc = Pattern.matches(pattern, item.getTitleStr());
            if(isMathc){
                matchItemIndex.add(item);
            }
        }
        for(Item item: matchItemIndex){
            if(clickedSpotItems.contains(item)) continue;
            else{
                clickedSpotItems.add(item);
            }
        }
    }

    private void goTherePlace(){
        ArrayList<Item> selectItem = new ArrayList<Item>();
        Item lastClickItem = null;
        long lastClickTime = 0;
        for(Item item: spotAllListViewItems){
            if(item.isClicked()){
                selectItem.add(item);
                if(item.getLastClickedTime() > lastClickTime){
                    lastClickItem = item;
                }
            }
        }
        if(selectItem.size() > 2 || selectItem.size() < 1){
            Toast toast = Toast.makeText(SceneQuery.this, "起点为当前位置，途径点，终点，依次为选择的顺序，至少选中一个地方，最多选中二个", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        String destination = "华南师范大学" + lastClickItem.getTitleStr();
        ArrayList<String> viaPoints  = new ArrayList<>();
        for(Item item: selectItem){
            if(item.getTitleStr().equals(lastClickItem.getTitleStr())) continue;
            viaPoints.add("华南师范大学" + item.getTitleStr());
        }
        BaiduMapUtil.setViaPoints(viaPoints);
        MyLog.d("viaPoints", viaPoints.toString()); //
        String api = BaiduMapUtil.apiBaiduMapRoutePlan(SceneQuery.this, "", destination, "walking");
        Intent ll = new Intent();
        ll.setData(Uri.parse(api));
        startActivity(ll);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    private void getSpotItems(){
        if(spotAllJson == null) return;
        try{
            //MyLog.d("jsonobj", spotAllJson.toString());
            JSONArray jsArr = spotAllJson.getJSONArray("教学科研机构");
            //MyLog.d("jsonarray", jsArr.toString());
            for(int i = 0; i < jsArr.length(); i++){
                JSONObject jsobj = jsArr.getJSONObject(i);
                Item item = new Item(R.drawable.scnu_icon, jsobj.getString("name"), jsobj.getString("intro"), SceneSpotModel.class);
                spotAllListViewItems.add(item);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
