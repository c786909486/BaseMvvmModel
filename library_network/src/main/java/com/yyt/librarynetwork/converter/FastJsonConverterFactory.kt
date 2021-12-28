package com.yyt.librarynetwork.converter

import retrofit2.Retrofit
import okhttp3.ResponseBody
import com.yyt.librarynetwork.converter.FastJsonResponseBodyConverter
import okhttp3.RequestBody
import com.yyt.librarynetwork.converter.FastJsonRequestBodyConverter
import com.yyt.librarynetwork.converter.FastJsonConverterFactory
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * Created by dawN4get on 2017/5/14.
 */
class FastJsonConverterFactory : Converter.Factory() {
    /**
     * 需要重写父类中responseBodyConverter，该方法用来转换服务器返回数据
     */
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, Any> {
        return FastJsonResponseBodyConverter(type)
    }

    /**
     * 需要重写父类中responseBodyConverter，该方法用来转换发送给服务器的数据
     */
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<Any, RequestBody> {
        return FastJsonRequestBodyConverter()
    }

    companion object {
        fun create(): FastJsonConverterFactory {
            return FastJsonConverterFactory()
        }
    }
}