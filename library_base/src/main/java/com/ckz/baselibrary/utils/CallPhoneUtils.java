package com.ckz.baselibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by hz-java on 2018/6/28.
 */

public class CallPhoneUtils {

    public static void callPhone(Context context,String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        context. startActivity(intent);
    }
}
