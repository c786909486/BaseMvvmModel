package com.ckz.library_base_compose.utils;

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



}
