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
               "身份验证错误!"
           }
           403 -> {
               "禁止访问!"
           }
           404 -> {
               "未找到该方法"
           }
           408 -> {
               "请求超时!"
           }
           503 -> {
               "服务器升级中!"
           }
           500 -> {
               "服务器内部错误!"
           }
           else -> {
               "网络连接失败，请检查网络"
           }
       }
    }
}