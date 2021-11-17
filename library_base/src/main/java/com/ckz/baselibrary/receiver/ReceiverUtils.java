package com.ckz.baselibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

public class ReceiverUtils {

    public static String action;
    private Context context;
    private Handler handler;
    private CommonReceiver receiver;


    public ReceiverUtils(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        action = context.getPackageName()+".CommonReceiver";
        receiver = new CommonReceiver();
    }

    public void registerReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        context.registerReceiver(receiver,filter);
    }

    public void unRegisterReceiver(){

        context.unregisterReceiver(receiver);
    }

    public static void sendFinish(Context context){
        Intent intent = new Intent(action);
        intent.putExtra("broadType",ReceiverConfig.FINISH_MSG);
        context.sendBroadcast(intent);
    }

    public class CommonReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("broadType",0);
            handler.sendEmptyMessage(type);
        }
    }
}
