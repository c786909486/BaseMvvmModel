package com.yyt.librarynetwork.utils

import com.yyt.librarynetwork.exception.ApiException

/**
 *@packageName com.yyt.librarynetwork.utils
 *@author kzcai
 *@date 2020/6/9
 */
object NetErrorString {

    fun getErrorStr(code:Int): String {
       return when (code) {
           401 -> {
               "身份验证错误!【${code}】"
           }
           403 -> {
               "禁止访问!【${code}】"
           }
           404 -> {
               "未找到该方法【${code}】"
           }
           408 -> {
               "请求超时!【${code}】"
           }
           503 -> {
               "服务器连接失败!【${code}】"
           }
           500 -> {
               "服务器内部错误!【${code}】"
           }
           else -> {
               "网络连接失败，请检查网络【${code}】"
           }
       }
    }
}