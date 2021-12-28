package com.yyt.librarynetwork.converter


import okhttp3.ResponseBody
import kotlin.Throws
import okio.BufferedSource
import com.alibaba.fastjson.JSON
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by dawN4get on 2017/5/14.
 */
class FastJsonResponseBodyConverter<T>(private val type: Type) : Converter<ResponseBody, T> {
    /*
     * 转换方法
     */
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val bufferedSource: BufferedSource = value.source().buffer()
        val tempStr = bufferedSource.readUtf8()
        bufferedSource.close()
        return JSON.parseObject(tempStr, type)
    }
}