package com.yyt.librarynetwork.converter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter

/**
 *@packageName com.yyt.librarynetwork.converter
 *@author kzcai
 *@date 2020/6/14
 */
class StringResponseBodyConverter:Converter<ResponseBody?,String>{
    override fun convert(value: ResponseBody?): String? {
        val json = "{\"jsonStr\":\"${value?.string()}\"}"
        return json
    }

}