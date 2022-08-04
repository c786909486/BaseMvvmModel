package com.yyt.librarynetwork.base

import android.database.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 *@packageName com.yyt.librarynetwork.base
 *@author kzcai
 *@date 2020/6/8
 */
interface CommonService {
    @GET
    suspend fun  getMethod(
        @Url url: String,
        @QueryMap options: Map<String, String?>?,
        @HeaderMap headers:Map<String,String>
    ): Map<String,Any?>

    @GET
    suspend fun  getMethod(@Url url: String): Map<String,Any?>?

    @GET
    suspend fun  getMethod(
        @Url url: String,
        @HeaderMap headers:Map<String,String>
    ): Map<String,Any?>


    @GET
    suspend fun getString(@Url url: String,@QueryMap options: Map<String, String?>?):ResponseBody

    @GET
    suspend fun getString(@Url url: String,@QueryMap options: Map<String, String?>?, @HeaderMap headers:Map<String,String>):ResponseBody

    @FormUrlEncoded
    @POST
    suspend fun postMethod(
        @Url url: String,
        @FieldMap options: Map<String, String>?
    ): Map<String,Any?>?

    @FormUrlEncoded
    @POST
    suspend fun postMethod(
        @Url url: String,
        @FieldMap options: Map<String, String>?,
        @HeaderMap headers:Map<String,String>
    ): Map<String,Any?>?

    @POST
    suspend fun postMethod(
        @Url url: String,
        @Body info: String,
        @HeaderMap headers:Map<String,String>
    ): Map<String,Any?>?

    @POST
    suspend fun postBody(
        @Url url: String,
        @Body info: Map<String,Any?>,
        @HeaderMap headers:Map<String,String>
    ): Map<String,Any?>?


    @POST
    suspend fun  post(
        @Url url: String,
        @QueryMap options: Map<String, String>?
    ): Map<String,Any?>?


    @POST
    suspend fun  uploadFile(
        @Url url: String,
        @Body body: RequestBody
    ):Map<String,Any?>?

    @POST
    suspend fun  uploadFile(
        @Url url: String,
        @Body body: RequestBody,
        @HeaderMap headers:Map<String,String>
    ):Map<String,Any?>?


    @Streaming
    @GET
    fun downloadFile(@Url url:String): Call<ResponseBody>
}