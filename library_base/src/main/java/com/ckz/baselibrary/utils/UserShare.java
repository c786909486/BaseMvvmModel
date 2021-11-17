package com.ckz.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ckz on 2018/5/2.
 */

public class UserShare {
    private static UserShare instance;
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private UserShare(Context context){
        this.context = context;
        preferences = context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static UserShare getInstance(Context context){
        if (instance == null){
            synchronized (UserShare.class){
                if (instance == null){
                    instance = new UserShare(context);
                }
            }
        }
        return instance;
    }

    public void saveUser(String userId){
        editor.putString("userId",userId);
        editor.commit();
    }

    public void saveUserName(String name){
        editor.putString("userName",name);
        editor.commit();
    }
    public void saveUserCount(String count){
        editor.putString("userCount",count);
        editor.commit();
    }

    public void savePassword(String password){
        editor.putString("password",password);
        editor.commit();
    }


    public void clearUser(){
        editor.clear().commit();
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

    public String getValue(String key){
        return preferences.getString(key,"");
    }
}
