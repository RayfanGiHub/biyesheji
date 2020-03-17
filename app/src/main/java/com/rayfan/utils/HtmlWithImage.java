package com.rayfan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import com.rayfan.biyesheji.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * HtmlWithImage.setContext(getApplicationContext()); // 必须设置
 *         HtmlWithImage.setShowBigImage(ShowBigImage.class); // 必须设置
 *         XueXiaoIntroductionTextView.setText(Html.fromHtml(HtmlWithImage.getHtmlString(R.raw.xuexiao_introduction),
 *                 new HtmlWithImage.AssetsImageGetter(XueXiaoIntroductionTextView), null));
 *         XueXiaoIntroductionTextView.setMovementMethod(HtmlWithImage.LinkMovementMethodExt.getInstance(HtmlWithImage.getShowBigImageHandler(), ImageSpan.class));
 */

public class HtmlWithImage {

    private static Class<? extends Activity> showBigImage;
    private static Context context;

    public static void setShowBigImage(Class<? extends Activity> _showBigImage) {
        showBigImage = _showBigImage;
    }

    public static void setContext(Context _context) {
        context = _context;
    }

    public static String getHtmlString(String filePath){
        if(filePath == null || filePath.equals("")) return "";
        return readFileToString(filePath);
    }

    public static String readFileToString(String filePath){
        /**
         * filePath   ----> R.raw.xuexiao_introduction
         */
        //InputStream in = context.getResources().openRawResource(filePath);
        InputStream in =null;
        try {
            in = context.getResources().getAssets().open(filePath);
        }catch (IOException e){
            e.printStackTrace();
        }


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



    public static class AssetsImageGetter implements Html.ImageGetter {
        private Context c;
        private TextView container;

        public AssetsImageGetter(TextView text){
            c = context;
            container = text;
        }
        @Override
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            InputStream is = null;
            //source便是图片的路径，如果图片在本地，可以这样做
            try {
//            is = c.getAssets().open(source);
                is = c.getResources().getAssets().open(source);
            } catch (IOException e) {
                MyLog.e("HtmlWithImage AssetsImageGetter IOException", e);
                e.printStackTrace();
            }
            try {
                TypedValue typedValue = new TypedValue();
                typedValue.density = TypedValue.DENSITY_DEFAULT;



                drawable = Drawable.createFromResourceStream(null, typedValue, is, "src");

                DisplayMetrics dm = c.getResources().getDisplayMetrics();
                int dwidth = dm.widthPixels-10;//padding left + padding right
                float dheight = (float)drawable.getIntrinsicHeight()*(float)dwidth/(float)drawable.getIntrinsicWidth();
                int dh = (int)(dheight+0.5);
                int wid = dwidth;
                int hei = dh;
                /*int wid = drawable.getIntrinsicWidth() > dwidth? dwidth: drawable.getIntrinsicWidth();
                int hei = drawable.getIntrinsicHeight() > dh ? dh: drawable.getIntrinsicHeight();*/
                drawable.setBounds(0, 0, wid, hei);


                //drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                return drawable;
            } catch (Exception e) {
                MyLog.e("HtmlWithImage AssetsImageGetter", e);
                return null;
            }
        }

    }

    public static Handler getShowBigImageHandler() {
        return new Handler() {

            public void handleMessage(Message msg) {
                if(context == null || showBigImage == null) return;
                int what = msg.what;
                if (what == 200) {
                    MessageSpan ms = (MessageSpan) msg.obj;
                    Object[] spans = (Object[]) ms.getObj();
                    final ArrayList<String> list = new ArrayList<>();
                    for (Object span : spans) {
                        if (span instanceof ImageSpan) {
                            MyLog.i("picUrl==", ((ImageSpan) span).getSource());
                            list.add(((ImageSpan) span).getSource());
                            Intent intent = new Intent(context, showBigImage);
                            intent.putStringArrayListExtra("images", list);
                            context.startActivity(intent);
                        }
                    }
                }
            }
        };
    }

    public static class LinkMovementMethodExt extends LinkMovementMethod {
        private static LinkMovementMethod sInstance;
        private Handler handler = null;
        private  Class spanClass = null;
        public static MovementMethod getInstance(Handler _handler, Class _spanClass) {
            if (sInstance == null) {
                sInstance = new LinkMovementMethodExt();
                ((LinkMovementMethodExt)sInstance).handler = _handler;
                ((LinkMovementMethodExt)sInstance).spanClass = _spanClass;
            }

            return sInstance;
        }

        int x1;
        int x2;
        int y1;
        int y2;

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer,
                                    MotionEvent event) {
            int action = event.getAction();

            if (event.getAction() == MotionEvent.ACTION_DOWN){
                x1 = (int) event.getX();
                y1 = (int) event.getY();
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                x2 = (int) event.getX();
                y2 = (int) event.getY();

                if (Math.abs(x1 - x2) < 10 && Math.abs(y1 - y2) < 10) {
                    x2 -= widget.getTotalPaddingLeft();
                    y2 -= widget.getTotalPaddingTop();
                    x2 += widget.getScrollX();
                    y2 += widget.getScrollY();
                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y2);
                    int off = layout.getOffsetForHorizontal(line, x2);
                    /**
                     * get you interest span
                     */
                    Object[] spans = buffer.getSpans(off, off, spanClass);
                    if (spans.length != 0) {

                        Selection.setSelection(buffer,
                                buffer.getSpanStart(spans[0]),
                                buffer.getSpanEnd(spans[0]));
                        MessageSpan obj = new MessageSpan();
                        obj.setObj(spans);
                        obj.setView(widget);
                        Message message = handler.obtainMessage();
                        message.obj = obj;
                        message.what = 200;
                        message.sendToTarget();
                        return true;
                    }
                }
            }
            //return false;
            return super.onTouchEvent(widget, buffer, event);
        }

        public boolean canSelectArbitrarily() {
            return true;
        }

        public boolean onKeyUp(TextView widget, Spannable buffer, int keyCode,
                               KeyEvent event) {
            return false;
        }
    }

    public static class MessageSpan {
        private Object obj;
        private TextView view;

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public TextView getView() {
            return view;
        }

        public void setView(TextView view) {
            this.view = view;
        }
    }
}
