package com.rayfan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rayfan.biyesheji.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageTitleIntroductionListAdapterUtil {
    /**
     *          <ListView
     *              android:id="@+id/listView"
     *              android:layout_width="match_parent"
     *              android:layout_height="wrap_content"/>
     *          ListView mListView = (ListView) findViewById(R.id.listView);
     *         final ImageTitleIntroductionListAdapterUtil listAdapterUtil = new ImageTitleIntroductionListAdapterUtil();
     *         listAdapterUtil.setContext(FacilityQuery.this);
     *         Item item_1 = new Item(
     *                 R.drawable.scnu_icon, R.string.infoservice_taoyuan,R.string.infoservice_activity_dushu_introduction, null);
     *         Item item_2 = new Item(
     *                 R.drawable.scnu_icon, R.string.infoservice_activity_dushu, R.string.infoservice_activity_dushu_introduction, SCNUAtivity.class);
     *         listAdapterUtil.appendItem(item_1);
     *         listAdapterUtil.appendItem(item_2);
     *         final ItemListAdapter itemListAdapter = listAdapterUtil.getItemListAdapter();
     *         mListView.setAdapter(itemListAdapter);
     *         mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     *             @Override
     *             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
     *                 if(view != null){
     *                     listAdapterUtil.onListItemClick(position);
     *                     itemListAdapter.notifyDataSetChanged(); // 通知重绘页面
     *                 }else{
     *                     MyLog.d("参数错误", listAdapterUtil.toString());
     *                 }
     *
     *             }
     *         });
     */
    private int[] itemImage;
    private int[] itemTitle;
    private int[] itemIntroduction;
    private Class<? extends Activity> [] itemActivity;
    private ArrayList<Item> items = new ArrayList<Item>();
    private Context context;
    public ImageTitleIntroductionListAdapterUtil() {}
    public void setContext(Context context) {
        this.context = context;
    }

    public void setItemActivity(Class<? extends Activity>[] activityClass){
        this.itemActivity = activityClass;
    }
    public Class<? extends Activity>[] getItemActivity(){
        return this.itemActivity;
    }
    public void setItemImage(int[] image){
        this.itemImage = image;
    }
    public int[] getItemImage() {
        return this.itemImage;
    }
    public void setItemTitle(int[] title){
        this.itemTitle = title;
    }
    public int[] getItemTitle(){
        return this.itemTitle;
    }
    public void setItemIntroduction(int[] introduction){
        this.itemIntroduction = introduction;
    }
    public int[] getItemIntroduction(){
        return this.itemIntroduction;
    }
    public boolean setItems(){
        if(itemImage == null || itemTitle == null || itemIntroduction == null || itemActivity == null){
            return false;
        }
        if(itemImage.length != itemTitle.length || itemTitle.length != itemIntroduction.length ||
                itemIntroduction.length != itemActivity.length){
            return false;
        }
        for(int i = 0; i < itemImage.length; i++){
            Item item = new Item(itemImage[i], itemTitle[i], itemIntroduction[i], itemActivity[i]);
            this.items.add(item);
        }
        return true;
    }

    public void setItems(ArrayList<Item> item){
        this.items = item;
    }

    public boolean setItems(int[] itemImage, int[] itemTitle, int[] itemIntroduction, Class<? extends Activity> [] itemActivity){
        if(itemImage == null || itemTitle == null || itemIntroduction == null || itemActivity == null){
            return false;
        }
        if( itemImage.length != itemTitle.length || itemTitle.length != itemIntroduction.length ||
                itemIntroduction.length != itemActivity.length){
            return false;
        }
        for(int i = 0; i < itemImage.length; i++){
            Item item = new Item(itemImage[i], itemTitle[i], itemIntroduction[i], itemActivity[i]);
            this.items.add(item);
        }
        return true;
    }

    public ArrayList<Item> getItems(){
        return this.items;
    }

    public void appendItem(Item item){
        this.items.add(item);
    }
    public Item removeItem(int index){
        return this.items.remove(index);
    }
    public void onListItemClick(int index){
        /**
         *  单击选中
         *  双击跳转
         */
        if(this.items.get(index).isDoubleClick()){
            // 双击
            if(this.items.get(index).itemClass == null) return; // null 时点击不做跳转
            Intent intent = new Intent(context, this.items.get(index).itemClass);
            context.startActivity(intent);
        }

    }
    public void onListItemClick(int index, String data){
        /**
         *  单击选中
         *  双击跳转
         */
        if(this.items.get(index).isDoubleClick()){
            // 双击
            if(this.items.get(index).itemClass == null) return; // null 时点击不做跳转
            Intent intent = new Intent(context, this.items.get(index).itemClass);
            intent.putExtra("data", dealData(index, data));
            context.startActivity(intent);
        }

    }
    private String dealData(int index, String data){
        JSONObject jsobj = null;
        try {
            jsobj = new JSONObject(data);
            String title = this.items.get(index).titleStr;
            JSONArray jsArr = jsobj.getJSONArray("教学科研机构");
            for(int i = 0; i < jsArr.length(); i++){
                JSONObject jsonObject = jsArr.getJSONObject(i);
                if(title.equals(jsonObject.getString("name"))){
                    return jsonObject.toString();
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            return "";
        }
        return "";

    }

    public ItemListAdapter getItemListAdapter() {
        return new ItemListAdapter();
    }
    public class ItemListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(context == null || items == null) return null;
            if (null == convertView) {
                convertView = View.inflate(context, R.layout.image_title_indtroduction_list_adapter_util, null);
            }
            ImageView imageView =(ImageView)convertView.findViewById(R.id.image);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.introduction);
            imageView.setBackgroundResource(items.get(position).getImage());
            if(!items.get(position).isString()){
                title.setText(items.get(position).getTitle());
                desc.setText(items.get(position).getIntroduction());
            }else{
                title.setText(items.get(position).getTitleStr());
                desc.setText(items.get(position).getIntroductionStr());
            }
            if(items.get(position).isClicked()){
                // 单击 变颜色
                title.setTextColor(Color.parseColor("#3197FF")); // 蓝色
                desc.setTextColor(Color.parseColor("#3197FF"));
            }else{
                title.setTextColor(Color.parseColor("#000000")); // 黑色
                desc.setTextColor(Color.parseColor("#000000"));
            }
            return convertView;
        }
    }
    public static class Item {
        private int image;
        private int title;
        private String titleStr;
        private int introduction;
        private String introductionStr;
        private boolean isClicked = false;
        private long lastClickedTime;
        private boolean isString = false;
        private Class<? extends Activity> itemClass;

        public Item(){}

        public Item(int image,int title, int introduction, Class<? extends Activity> itemClass) {
            this.image = image;
            this.title = title;
            this.introduction = introduction;
            this.itemClass = itemClass;
        }
        public Item(int image, String title, String introduction, Class<? extends Activity> itemClass){
            this.image = image;
            this.titleStr = title;
            this.introductionStr = introduction;
            this.itemClass = itemClass;
            this.isString = true;
        }

        public boolean isClicked() {
            return isClicked;
        }

        public void setClicked(boolean clicked) {
            isClicked = clicked;
        }

        public long getLastClickedTime() {
            return lastClickedTime;
        }

        public void setImage(int image){
            this.image = image;
        }
        public int getImage() {
            return image;
        }
        public int getTitle() {
            return title;
        }
        public void setTitle(int title) {
            this.title = title;
        }
        public int getIntroduction() {
            return introduction;
        }
        public void setIntroduction(int introduction) {
            this.introduction = introduction;
        }

        public String getTitleStr() {
            return titleStr;
        }

        public void setTitleStr(String titleStr) {
            this.titleStr = titleStr;
        }

        public String getIntroductionStr() {
            return introductionStr;
        }

        public void setIntroductionStr(String introductionStr) {
            this.introductionStr = introductionStr;
        }

        public boolean isString() {
            return isString;
        }

        public void setString(boolean string) {
            isString = string;
        }

        public Class<? extends Activity> getItemClass() {
            return itemClass;
        }
        public void setItemClass(Class<? extends Activity> itemClass) {
            this.itemClass = itemClass;
        }
        public boolean isDoubleClick(){
            long clickedTime = System.currentTimeMillis();
            if(clickedTime - lastClickedTime > 500){
                // 单击
                lastClickedTime = clickedTime;
                isClicked = !isClicked;
                return false;
            }
            return true;
        }
    }

    @Override
    public String toString() {
        return "ImageTitleIntroductionListAdapterUtil{" +
                "itemImage=" + Arrays.toString(itemImage) +
                ", itemTitle=" + Arrays.toString(itemTitle) +
                ", itemIntroduction=" + Arrays.toString(itemIntroduction) +
                ", itemActivity=" + Arrays.toString(itemActivity) +
                ", items=" + items +
                ", context=" + context +
                '}';
    }

}
