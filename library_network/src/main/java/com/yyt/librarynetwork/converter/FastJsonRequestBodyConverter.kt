package com.yyt.librarynetwork.converter

import okhttp3.RequestBody
import kotlin.Throws
import com.yyt.librarynetwork.converter.FastJsonRequestBodyConverter
import com.alibaba.fastjson.JSON
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Converter
import java.io.IOException

/**
 * Created by dawN4get on 2017/5/14.
 */
class FastJsonRequestBodyConverter<T> : Converter<T, RequestBody> {
    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value))
    }

    companion object {
        private val MEDIA_TYPE: MediaType = "application/json; charset=UTF-8".toMediaTypeOrNull()!!
    }
}