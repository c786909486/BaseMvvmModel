package com.ckz.library_base_compose.utils;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class QuitUtils {

    // 当前按下返回按钮的时间
    private long mExitTime = 0;
    // 双击返回键之间的延迟
    private static final int EXIT_TIME = 2000;

    private Activity activity;

    public QuitUtils(Activity activity) {
        this.activity = activity;
    }

    private static QuitUtils instance;

    public static QuitUtils init(Activity activity){
        return new QuitUtils(activity);
    }

    public void bindWithView(View view){
        view.setOnKeyListener( OnKeyDown);
    }

    public void doubleQuit(){
        if ((System.currentTimeMillis() - mExitTime) > EXIT_TIME) {
            Toast.makeText(activity, "再点击一次推出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            activity.finish();
            if (mExitTime != 0) {
                mExitTime = 0;
            }
        }
    }


    private View.OnKeyListener OnKeyDown = new View.OnKeyListener() {
       @Override
       public boolean onKey(View v, int keyCode, KeyEvent event) {
           if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
               if (v instanceof WebView){
                   if (((WebView) v).canGoBack()){
                       ((WebView) v).goBack();
                   }else {
                       if ((System.currentTimeMillis() - mExitTime) > EXIT_TIME) {
                           Toast.makeText(activity, "双击退出", Toast.LENGTH_SHORT).show();
                           mExitTime = System.currentTimeMillis();
                       } else {
                           activity.finish();
                           if (mExitTime != 0) {
                               mExitTime = 0;
                           }
                       }
                   }
               }else {
                   if ((System.currentTimeMillis() - mExitTime) > EXIT_TIME) {
                       Toast.makeText(activity, "双击退出", Toast.LENGTH_SHORT).show();
                       mExitTime = System.currentTimeMillis();
                   } else {
                       activity.finish();
                       if (mExitTime != 0) {
                           mExitTime = 0;
                       }
                   }
               }
               return true;
           }
           return false;
       }
   };


}
