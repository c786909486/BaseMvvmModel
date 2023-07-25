package com.yyt.buffetposapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION
import android.net.wifi.WifiManager.WIFI_STATE_CHANGED_ACTION
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import com.ckz.baselibrary.utils.LogUtils
import java.io.BufferedReader
import java.io.InputStreamReader


/**
 *@packageName com.ckz.baselibrary.utils
 *@author kzcai
 *@date 2022/11/25
 */
object NetWorkUtils {

    val NETWORKTYPE_NONE = -1
    val NETWORKTYPE_WIFI = 0
    val NETWORKTYPE_2G = 1
    val NETWORKTYPE_4G = 2
    val NETWORKTYPE_ETHERNET = 3



    private var p:Process?=null

    fun getNetDelayAndLost(host:String,pingNumber:Int){
        var lost = String()
        var delay = String()
        p = Runtime.getRuntime().exec("ping -c $pingNumber $host")
        val buf = BufferedReader(InputStreamReader(p!!.inputStream))
        var str = String()
        try {
            while (buf.readLine()?.also { str = it } != null) {
                LogUtils
                    .d("NetWorkUtils","receive===>${str}")
                if (str.contains("packet loss")) {
                    val i = str.indexOf("received")
                    val j = str.indexOf("%")
                    //                  System.out.println("丢包率:"+str.substring(j-3, j+1));
                    lost = str.substring(i + 10, j + 1)

                    for (listener in listeners){
                        listener.onGetLostInfo(lost = lost)
                    }
                }
                if (str.contains("avg")) {
                    val i = str.indexOf("/", 20)
                    val j = str.indexOf(".", i)

                    delay = str.substring(i + 1, j)
                    delay = delay + "ms"

                    for (listener in listeners){
                        listener.onGetNetDelay(delay = delay)
                    }
                }
            }

        }catch (e:Exception){
            e.printStackTrace()
            for (listener in listeners){
                listener.onError(e)
            }
        }

    }

    fun getNetDelay(host: String){
        p = Runtime.getRuntime().exec("ping $host")
        val buf = BufferedReader(InputStreamReader(p!!.inputStream))
        var str = String()
        try {
            while (buf.readLine()?.also { str = it } != null) {
                LogUtils
                    .d("NetWorkUtils","receive===>${str}")
                if (str.indexOf("time=")!=-1){
                    val item = str.split("time=")
                    var delay =  item[1].replace("ms","").trim()
                    for (listener in listeners){
                        listener.onGetNetDelay(delay)
                    }
                }else{
                    for (listener in listeners){
                        listener.onError(Exception(str))
                    }
                    Log.d("NetWorkUtils", "getNetDelay: ${str}")
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("NetWorkUtils", "getNetDelay: ${str}")

            for (listener in listeners){
                listener.onError(e)
            }
        }
    }


    fun getWifiInfo(context: Context): WifiInfo? {
        if (Build.VERSION.SDK_INT>=31){
            if (getNetWorkType(context) != NETWORKTYPE_WIFI) {
                return null
            }

            val connectivityManager =
                context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
            val netWork = connectivityManager!!.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(netWork)
            return networkCapabilities!!.transportInfo as WifiInfo?
        }else {
            val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
            return wifiManager.connectionInfo
        }
    }

    fun getNetWorkType(context: Context): Int {
        var mNetWorkType = NETWORKTYPE_NONE
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT<23){
            val networkInfo: NetworkInfo? = manager.activeNetworkInfo
            if (networkInfo != null && networkInfo.isConnected()) {
                val type: String = networkInfo.getTypeName()
                if (type.equals("WIFI", ignoreCase = true)) {
                    mNetWorkType =  NETWORKTYPE_WIFI
                } else if (type.equals("MOBILE", ignoreCase = true)) {
                    return if (isFastMobileNetwork(context)) NETWORKTYPE_4G else NETWORKTYPE_2G
                }else if (type.equals("ETHERNET", ignoreCase = true)) {
                    return NETWORKTYPE_ETHERNET
                }
            } else {
                mNetWorkType = NETWORKTYPE_NONE //没有网络
            }
        }else{
            val networkInfo = manager.activeNetwork
            if (networkInfo != null) {
                val  nc=manager.getNetworkCapabilities(networkInfo);
                if(nc!=null){
                    if(nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){//WIFI
                        mNetWorkType = NETWORKTYPE_WIFI
                    }else if(nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){//移动数据
                        mNetWorkType =  if (isFastMobileNetwork(context)) NETWORKTYPE_4G else NETWORKTYPE_2G
                    }else if(nc.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){//移动数据
                        mNetWorkType =  NETWORKTYPE_ETHERNET
                    }
                }

            } else {
                mNetWorkType = NETWORKTYPE_NONE //没有网络
            }
        }
        return mNetWorkType
    }

    /**
     * 判断网络类型
     */
    private fun isFastMobileNetwork(context: Context): Boolean {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return try {
            telephonyManager.dataNetworkType == TelephonyManager.NETWORK_TYPE_LTE
        }catch (e:Exception){
            false
        }
    }


    private val listeners = mutableListOf<OnNetWorDelayListener>()

    fun addListener(listener: OnNetWorDelayListener){
        listeners.add(listener)
    }

    fun removeListener(listener: OnNetWorDelayListener){
        listeners.remove(listener)
    }

    fun stop(){
        p?.destroy()
        p = null
    }

    interface OnNetWorDelayListener{
        fun onGetLostInfo(lost:String)
        fun onGetNetDelay(delay:String)
        fun onError(error:Exception)
    }

    var receiver = NetChangeReceiver()

    fun addNetChangeListener(listener:(()->Unit)){
        receiver.onChanged = listener
    }

    fun registerNetChange(context: Context){
        val intent = IntentFilter()
        intent.addAction(WIFI_STATE_CHANGED_ACTION)
        intent.addAction(NETWORK_STATE_CHANGED_ACTION)
        intent.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(receiver,intent)
    }

    fun unregisterNetChange(context: Context){
        receiver.onChanged = null
        context.unregisterReceiver(receiver)
    }

    class NetChangeReceiver:BroadcastReceiver(){

        var onChanged:(()->Unit)?=null

        override fun onReceive(p0: Context?, p1: Intent?) {
            when(p1?.action){
                WIFI_STATE_CHANGED_ACTION,
                NETWORK_STATE_CHANGED_ACTION,
                ConnectivityManager.CONNECTIVITY_ACTION->{
                    onChanged?.invoke()
                }
            }

        }

    }
}