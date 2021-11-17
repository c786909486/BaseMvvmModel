package com.ckz.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by ckz on 2018/5/2.
 */

public class AxunUser {

    public static String userId;
    public static String userName;
    public static String userAccount;
    public static boolean isLogin;

    private static AxunUser instance;
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private AxunUser(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(context.getPackageName()+".AxunUser",Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static AxunUser getInstance(Context context){
        if (instance == null){
            synchronized (AxunUser.class){
                if (instance == null){
                    instance = new AxunUser(context);
                }
            }
        }
        return instance;
    }

    public static void init(Context context){
        AxunUser user = AxunUser.getInstance(context);
        userId = user.getUserId();
        userName = user.getUserName();
        userAccount = user.getAccount();

        isLogin = !TextUtils.isEmpty(user.getUserId());
    }

    public AxunUser saveUserId(String userId){
        AxunUser.userId = userId;
        editor.putString("userId",userId);
        editor.commit();
        return this;
    }

    public AxunUser saveUserName(String name){
        AxunUser.userName = name;
        editor.putString("userName",name);
        editor.commit();
        return this;
    }
    public AxunUser saveUserAccount(String account){
        AxunUser.userAccount = account;
        editor.putString("userAccount",account);
        editor.commit();
        return this;
    }

    public AxunUser savePassword(String password){
        editor.putString("password",password);
        editor.commit();
        return this;
    }

    public void commit(){
        init(context);
    }


    public void clearUser(){
        editor.clear().commit();
        init(context);
    }

    public String getUserId(){
        return preferences.getString("userId","");
    }

    public String getSessionId(){
        return preferences.getString("sessionId","");
    }

    public String getUserName(){
        return preferences.getString("userName","");
    }

    public String getKey(){
        return preferences.getString("key","");
    }

    public String getAccount(){
        return preferences.getString("userAccount","");
    }

    public String getValue(String key){
        return preferences.getString(key,"");
    }
}
