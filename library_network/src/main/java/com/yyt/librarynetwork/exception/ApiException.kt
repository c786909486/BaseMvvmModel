package com.yyt.librarynetwork.exception

import org.json.JSONException
import java.lang.RuntimeException

/**
 *@packageName com.yyt.library_network.exception
 *@author kzcai
 *@date 2020/6/8
 */
class ApiException(message: String?) : Exception(message) {
    companion object {
        fun getErrorInfo(throwable: Throwable): String? {
            if (throwable is ApiException) {
                return throwable.message
            }
            return if (throwable is JSONException) {
                "数据异常"
            } else "网络或服务器异常"
        }
    }
}