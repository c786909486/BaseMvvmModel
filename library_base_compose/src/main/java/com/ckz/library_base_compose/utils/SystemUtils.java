package com.ckz.library_base_compose.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * @author kzcai
 * @packageName com.axun.yytmanage.standard.utils
 * @date 2019-05-21
 */
public class SystemUtils {
    public static final String TAG="SystemUtils";
    public static boolean isAppaLive(Context context , String str) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        //String MY_PKG_NAME = "你的包名";
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(str)//如果想要手动输入的话可以str换成<span style="font-family: Arial, Helvetica, sans-serif;">MY_PKG_NAME，下面相同</span>
                    || info.baseActivity.getPackageName().equals(str)) {
                isAppRunning = true;
                Log.i(TAG, info.topActivity.getPackageName()
                        + " info.baseActivity.getPackageName()="
                        + info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }
}
