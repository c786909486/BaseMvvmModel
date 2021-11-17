package com.yyt.librarynetwork.converter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.io.StringReader

/**
 *@packageName com.yyt.librarynetwork.converter
 *@author kzcai
 *@date 2020/6/14
 */
class MyGsonResponseBodyConverter<T:Any>(
    gson: Gson,
    adapter: TypeAdapter<out Any>
) :
    Converter<ResponseBody?, T?> {
    private val gson: Gson
    private val adapter: TypeAdapter<T>

    @Throws(IOException::class)
    override fun convert(value: ResponseBody?): T? {
//        val json = "{\"data\":${value?.string()}}"
        val json = value?.string()
//        Log.d("asqda",json)
        val jsonReader = gson.newJsonReader(StringReader(json))
        return try {
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            result
        } finally {
            value?.close()
        }
    }

    init {
        this.gson = gson
        this.adapter = adapter as TypeAdapter<T>
    }
}
