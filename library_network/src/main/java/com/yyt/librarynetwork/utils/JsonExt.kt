package com.yyt.librarynetwork.utils

import com.google.gson.Gson
import com.yyt.librarynetwork.interceptor.ApiErrorHelper

/**
 * 使用Accessor来解析json的方法
 */
inline fun <reified K> String.parseAccessorJson(): K {
    return Gson().fromJson(this,K::class.java)
}

inline fun <reified K> Map<String,Any?>?.toObject():K{
    val gson = Gson()
    return gson.fromJson(gson.toJson(this),K::class.java)
}

/**
 * 使用Accessor来序列化json的方法
 */
fun Any.toAccessorJson(): String {
    return Gson().toJson(this)
}

fun Exception.toNetError():String{
    return ApiErrorHelper.netErrorString(this)
}

