package com.yyt.librarynetwork.interceptor

import android.content.Context
import android.widget.Toast
import com.yyt.librarynetwork.exception.ApiException
import com.yyt.librarynetwork.utils.NetErrorString
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException

/**
 *@packageName com.yyt.library_network.interceptor
 *@author kzcai
 *@date 2020/6/8
 */
//辅助处理异常
object ApiErrorHelper {
    fun handleCommonError(context: Context?, e: Throwable?) {
        when (e) {
            is HttpException -> {
                val code = (e as HttpException).response()?.code()
                try {
                    if (code!=null){
                        Toast.makeText(context, NetErrorString.getErrorStr(code), Toast.LENGTH_SHORT).show()
                    }
                }catch (e:IOException){
                    e.printStackTrace()
                }

            }
            is IOException -> {
                Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show()
            }
            is ApiException -> {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

            is JSONException ->{
                Toast.makeText(context,"JSON解析错误",Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "未知错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun netErrorString( e: Throwable?):String {
        when (e) {
            is HttpException -> {
                val code = e.response()?.code()
                try {
                    if (code!=null){
                        return  NetErrorString.getErrorStr(code)
                    }
                }catch (e:IOException){
                    e.printStackTrace()
                }

            }
            is IOException -> {
                return "连接失败:${e.message}"
            }
            is ApiException -> {
                return  e.message?:""
            }

            is JSONException ->{
                return  "JSON解析错误:${e.message}"
            }
            else -> {
                return e.toString()
            }
        }
        return e.toString()
    }

}