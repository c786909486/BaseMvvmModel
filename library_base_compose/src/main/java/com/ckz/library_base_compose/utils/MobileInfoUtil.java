package com.ckz.library_base_compose.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

public class MobileInfoUtil {

    public static  String getIMEI(Context context) {
        try { //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //获取IMEI号
            String imei = telephonyManager.getDeviceId(); //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint({"NewApi", "MissingPermission"})
    public static String getSerialNumber(Context context) {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT>=28){
                serial = Settings.System.getString(
                        context.getContentResolver(), Settings.Secure.ANDROID_ID);
//            }else if (Build.VERSION.SDK_INT == 28) {
//                serial = Build.getSerial();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                serial = Build.SERIAL;
            } else {//8.0-
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("e", "读取设备序列号异常：" + e.toString());
        }
        return serial;
    }

}
