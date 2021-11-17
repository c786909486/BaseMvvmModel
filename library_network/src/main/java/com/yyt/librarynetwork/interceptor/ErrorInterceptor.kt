package com.yyt.librarynetwork.interceptor

import com.yyt.librarynetwork.exception.ApiException
import okhttp3.Interceptor
import okhttp3.Response

/**
 *@packageName com.yyt.library_network.interceptor
 *@author kzcai
 *@date 2020/6/8
 */
class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val response = chain.proceed(request)


        return response
    }
}