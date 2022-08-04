package com.yyt.librarynetwork

import android.util.Log
import com.yyt.librarynetwork.base.CommonService
import com.yyt.librarynetwork.converter.FastJsonConverterFactory
import com.yyt.librarynetwork.cookie.SimpleCookieJar
import com.yyt.librarynetwork.https.HttpsUtils
import com.yyt.librarynetwork.listener.OnDownloadListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

/**
 *@packageName com.yyt.library_network
 *@author kzcai
 *@date 2020/6/8
 */
class HttpManager {


    var retrofitBuilder: Retrofit.Builder? = null

    var okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()


    private val baseUrls: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    fun addBaseUrl(key: String, url: String): HttpManager {
        baseUrls[key] = url
        return this
    }

    var baseUrl: String? = null
    fun setBaseUrl(baseUrl: String?): HttpManager {
        this.baseUrl = baseUrl
        return this
    }

    /*读取超时时间*/
    var readTimeout: Long = 60 * 1000
    fun setReadTimeOut(readTimeOut: Long): HttpManager {
        this.readTimeout = readTimeOut
        return this
    }

    /*写入超时时间*/
    var writeTimeOut: Long = 60 * 1000
    fun setWriteTimeOut(writeTimeOut: Long): HttpManager {
        this.writeTimeOut = writeTimeOut
        return this
    }

    /*连接超时时间*/
    var connectTimeOut: Long = 60 * 1000
    fun setConnectTimeOut(connectTimeOut: Long): HttpManager {
        this.connectTimeOut = connectTimeOut
        return this
    }


    private fun initOkHttp() {

        okHttpClientBuilder = okHttpClientBuilder ?: OkHttpClient.Builder()

        okHttpClientBuilder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
            .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
            .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
            .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
//                .addInterceptor(ErrorInterceptor())
//                .addInterceptor(initLogInterceptor())
//            .cookieJar(SimpleCookieJar())
            .followRedirects(true)
            .sslSocketFactory(
                HttpsUtils.initSSLSocketFactory(),
                HttpsUtils.initTrustManager()
            )

//        okHttpClient = okHttpClientBuilder.build()

        initRetrofit()

    }

    fun addInterceptor(interceptor: Interceptor): HttpManager {
            okHttpClientBuilder
                .addInterceptor(interceptor)
        return this
    }


    private fun initRetrofit() {

        if (retrofitBuilder == null) {
            retrofitBuilder = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl ?: "")
                .addConverterFactory(FastJsonConverterFactory.create())

        } else {
            retrofitBuilder!!
                .client(okHttpClient)
                .baseUrl(baseUrl ?: "")
                .addConverterFactory(FastJsonConverterFactory.create())
        }


    }


    /*
    * 日志拦截器
    * */
    private fun initLogInterceptor(): HttpLoggingInterceptor {

        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.i("Retrofit", message)
            }
        })

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }


    companion object {
        val instance: HttpManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpManager()
        }

        val okHttpClient: OkHttpClient get() = instance.okHttpClientBuilder.build()

        val retrofit: Retrofit
            get() = instance.retrofitBuilder!!.build()

        private val FILE_TYPE: MediaType? = "application/octet-stream".toMediaTypeOrNull()


    }

    /**
     * 标准 get 请求
     */
    suspend fun get(url: String): Map<String, Any?>? {
        return retrofit?.create(CommonService::class.java)?.getMethod(url)
    }

    suspend fun get(url: String,headers: Map<String, String>): Map<String, Any?>? {
        return retrofit?.create(CommonService::class.java)?.getMethod(url,headers)
    }


    suspend fun get(url: String, options: Map<String, String>?,headers: Map<String, String>): Map<String, Any?>? {
        return retrofit?.create(CommonService::class.java)?.getMethod(url, options ?: HashMap(),headers)
    }

    suspend fun getString(url: String, options: Map<String, String>?): String {
        val call = retrofit.create(CommonService::class.java).getString(
            url, options ?: HashMap()
        )
        return call.string()
    }

    /**
     * 标准 post 请求
     */
    suspend fun postMethod(url: String, options: Map<String, String>): Map<String, Any?>? {
        return retrofit.create(CommonService::class.java).postMethod(url, options)
    }

    suspend fun postMethod(url: String, options: Map<String, String>,headers:Map<String,String>): Map<String, Any?>? {
        return retrofit.create(CommonService::class.java).postMethod(url, options,headers)
    }

    suspend fun postMethod(url: String, info: String,headers:Map<String,String>): Map<String, Any?>? {
        val requestBody = info.toRequestBody("application/json; Accept: application/json".toMediaTypeOrNull())
        return retrofit.create(CommonService::class.java).postMethod(url, requestBody,headers)
    }

//    suspend fun postBody(url: String, info: Map<String,Any?>,headers:Map<String,String>): Map<String, Any?>? {
//        return retrofit.create(CommonService::class.java).postBody(url, info,headers)
//    }


    fun webSocket(url: String, listener: WebSocketListener): WebSocket {
        val client = okHttpClient.newBuilder()
            .pingInterval(20, TimeUnit.SECONDS)
            .build()
        val resueqt = Request.Builder().url(url).build()
        return client.newWebSocket(resueqt, listener)
    }

    /**
     * 标准 post 请求
     */
    suspend fun post(url: String, options: Map<String, String>?): Map<String, Any?>? {

        return retrofit?.create(CommonService::class.java)
            ?.post(url, options ?: HashMap<String, String>())
    }

    suspend fun uploadFile(url: String, options: Map<String, Any?>): Map<String, Any?>? {
        val builder: MultipartBody.Builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        for (item in options.entries) {
            if (item.value is File) {
                builder.addFormDataPart(
                    item.key, (item.value as File).name, (item.value as File).asRequestBody(
                        FILE_TYPE
                    )
                )
            } else {
                builder.addFormDataPart(item.key, item.value as String)
            }
        }
        return retrofit.create(CommonService::class.java)
            .uploadFile(url, builder.build())
    }

    suspend fun downloadFile(fileUrl: String, saveFile: String, listener: OnDownloadListener) {
        withContext(Dispatchers.IO) {

            val file = File(saveFile)
            file.createNewFile()
            withContext(Dispatchers.Main) {
                listener.onDownloadStart()
            }
            val call = retrofit.create(CommonService::class.java)
                .downloadFile(fileUrl)
            val response: Response<ResponseBody>
            val body: ResponseBody?
            try {
                response = call.execute()
                body = response.body()
            } catch (e: Exception) {
                listener.onDownloadFail("无法连接网络")
                return@withContext
            }


            if (response.isSuccessful && body != null) {
                var inStream: InputStream? = null
                var outStream: OutputStream? = null
                /*注意,在kotlin中没有受检异常,
                    如果这里不写try catch,编译器也是不会报错的,
                    但是我们需要确保流关闭,所以需要在finally进行操作*/
                try {
                    //以下读写文件的操作和java类似
                    inStream = body.byteStream()
                    outStream = file.outputStream()
                    //文件总长度
                    val contentLength = body.contentLength()
                    //当前已下载长度
                    var currentLength = 0L
                    //缓冲区
                    val buff = ByteArray(1024)
                    var len = inStream.read(buff)
                    var percent = 0f
                    while (len != -1) {
                        outStream.write(buff, 0, len)
                        currentLength += len
                        /*不要频繁的调用切换线程,否则某些手机可能因为频繁切换线程导致卡顿,
                        这里加一个限制条件,只有下载百分比更新了才切换线程去更新UI*/
//                        Log.d("downloadProcess",(currentLength / contentLength * 100).toString()+"\n总大小："+contentLength+"\n已下载："+currentLength)
                        if ((currentLength * 100 / contentLength).toInt() > percent) {
                            percent =
                                (currentLength.toFloat() / contentLength.toFloat() * 100).toFloat()
                            //切换到主线程更新UI
                            withContext(Dispatchers.Main) {
                                //todo:下载中
                                listener.onDownloading(
                                    percent.toInt(),
                                    currentLength,
                                    contentLength
                                )
                            }
                            //更新完成UI之后立刻切回IO线程
                        }

                        len = inStream.read(buff)
                    }
                    //下载完成之后,切换到主线程更新UI
                    withContext(Dispatchers.Main) {
                        //todo：下载完成
                        listener.onDownloadFinish()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    //todo:下载失败
                    listener.onDownloadFail(e.toString())

                } finally {
                    inStream?.close()
                    outStream?.close()

                }
            } else {
                //todo:请求失败
            }


        }
    }

    /**
     * 支持用户使用自己定义的 RetrofitService，而非公共的 RetrofitService
     */
    fun <S> createRetrofitService(service: Class<S>): S {
        return retrofit!!.create(service)
    }


    fun setProxy(proxy: Proxy): HttpManager {
        instance.okHttpClientBuilder.proxy(proxy)
        return this
    }

    fun setProxy(hostName: String, port: Int): HttpManager {
        val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(hostName, port))
        setProxy(proxy)
        return this
    }

    fun build(): HttpManager {
        if (!baseUrl.isNullOrEmpty()) {
            addBaseUrl("defaultUrl", baseUrl!!)
        }

        initOkHttp()
        return HttpManager.instance
    }

    /**
     * https的全局访问规则
     */
    fun setHostnameVerifier(hostnameVerifier: HostnameVerifier?): HttpManager {
        okHttpClientBuilder.hostnameVerifier(hostnameVerifier!!)
        return this
    }

    /**
     * https的全局自签名证书
     */
    fun setCertificates(vararg certificates: InputStream?): HttpManager {
        val sslParams = HttpsUtils.getSslSocketFactory(null, null, certificates)
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
        return this
    }

    /**
     * https双向认证证书
     */
    fun setCertificates(
        bksFile: InputStream?,
        password: String?,
        vararg certificates: InputStream?
    ): HttpManager {
        val sslParams = HttpsUtils.getSslSocketFactory(bksFile, password, certificates)
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
        return this
    }
}

