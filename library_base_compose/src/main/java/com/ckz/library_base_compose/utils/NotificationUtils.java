package com.ckz.library_base_compose.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;

import static android.content.Context.NOTIFICATION_SERVICE;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;

public class NotificationUtils {

    private static String id = "my_channel_01";
    private static String name="报警通知";

    public static void showNotification(Context context, Class clazz, String title, String message, @DrawableRes int big, @DrawableRes int small) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableVibration(true);
            mChannel.enableLights(true);
            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            mChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
            notification = new NotificationCompat.Builder(context,id)
                    /**设置通知左边的大图标**/
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), big))
                    /**设置通知右边的小图标**/
                    .setSmallIcon(small)
                    .setTicker(message)
                    .setContentTitle(title)
                    .setContentText(message)
                    /**通知产生的时间，会在通知信息里显示**/
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
//                .setOngoing(false)
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, clazz), PendingIntent.FLAG_CANCEL_CURRENT))
                    .build();

        } else {
            notification = new NotificationCompat.Builder(context,id)
                    /**设置通知左边的大图标**/
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), big))
                    /**设置通知右边的小图标**/
                    .setSmallIcon(small)
                    .setTicker(message)
                    .setContentTitle(title)
                    .setContentText(message)
                    /**通知产生的时间，会在通知信息里显示**/
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    /**设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)**/
//                .setOngoing(false)
                    /**向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：**/
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, clazz), PendingIntent.FLAG_CANCEL_CURRENT))
                    .build();
        }
        /**发起通知**/
        notificationManager.notify(0x123, notification);

    }
}
