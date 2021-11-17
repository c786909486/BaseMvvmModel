package com.ckz.baselibrary.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;

import androidx.annotation.ColorRes;

/**
 * Created by hz-java on 2018/8/6.
 */

public class LigntBarUtils {

    public static void  lightBar(Window window){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }



    public static void setBarInFragment(Activity activity, View view){
        StatusBarUtil.setTranslucentForImageViewInFragment(activity,0,view,true);
        lightBar(activity.getWindow());
    }

    public static void setBar(Activity activity){
        StatusBarUtil.setTranslucent(activity,0);
        lightBar(activity.getWindow());
    }


    public static void setBarInWhite(Activity activity){
        StatusBarUtil.setColor(activity,0xffffffff,0);
        lightBar(activity.getWindow());
    }

    public static void setBarInColor(Activity activity, @ColorRes int colorResId){
        StatusBarUtil.setColor(activity,activity.getResources().getColor(colorResId),0);
//        lightBar(activity.getWindow());
    }
}
