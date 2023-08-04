package com.axun.basemvvmmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.axun.library_update.update.UpdateUtils
import com.ckz.baselibrary.base.BaseViewModel
import com.ckz.baselibrary.message.MessageEvent
import com.ckz.baselibrary.utils.NetWorkUtils
import com.yyt.librarynetwork.HttpManager
import com.yyt.librarynetwork.utils.toAccessorJson
import com.yyt.librarynetwork.utils.toNetError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

/**
 *@packageName com.axun.basemvvmmodel
 *@author kzcai
 *@date 2022/6/10
 */
class MainViewModel(application: Application) : BaseViewModel(application) {

    fun sendToast(view:View){
        val message = MessageEvent(111111)
        message.obj = "1231"
        sendMessage(message)
    }

    override fun onMessageEvent(event: MessageEvent) {
        super.onMessageEvent(event)
        if (event.code==1){
            showToast(event.obj.toString())
        }
    }

    fun sendMessage(event: MessageEvent) {
        EventBus.getDefault().post(event)
    }

    fun netClick(view: View) {
        viewModelScope.launch {
            showDialog()
            try {
                getJsonNet()
            } catch (e: Exception) {
                e.printStackTrace()
                showToast(e.toNetError())
            }
            dismissDialog()
        }
    }

    fun appUpdate(view:View){
        val model = AppUpdateData("1.0.2","http://42.193.160.4:8084/adm/app/lishiApp.apk")
        UpdateUtils.instance.checkUpdate(model,true)
    }

    suspend fun getNet() {
        val url = "http://ageds.rkph.com.cn:8081/interfaces/consumptionApp.do?m=payByAlipay"
        val json =
            "{\"userId\":\"109\",\"snCode\":\"TN1119C340016\",\"payTypeId\":\"130001\",\"receivableAmount\":\"1.00\",\"paidAmount\":\"1\",\"cCardId\":\"2c9e9f8c81181358018118efb6c80090\",\"clubBillId\":\"2c9e9f8c814906ef01814bb0e1b90091\",\"name\":\"涌泉蜜桔\",\"remark\":\"\",\"paymentRemark\":\"\",\"discount\":\"100\",\"isBearDiscount\":\"0\",\"isDisDiscount\":\"0\",\"detailDiscount\":\"100\",\"auth_code\":\"99999123123\"}"
        val data = JSON.parseObject(json)
        var map = data.innerMap
        val header = mapOf(
            "clientType" to "app",
            "content-type" to "application"
        )
        val response = HttpManager.instance.postMethod(url, map.toJsonMap(),header)
    }

    suspend fun getJsonNet(){
        val url = "http://ageds.rkph.com.cn:8081/interfaces/consumptionApp.do?m=payByAlipay"
        var map = mapOf(
            "aapp" to "111",
            "sss" to "1ww"
        )
        val header = mapOf(
            "clientType" to "app",
            "content-type" to "application/json"
        )
        val response = HttpManager.instance.postMethod(url,map.toAccessorJson(),header)
    }

    fun netTest(view:View){
        viewModelScope.launch {
            withContext(Dispatchers.IO){

                NetWorkUtils.getNetDelay("www.axun.com.cn")
            }
        }

    }

    fun stopDelay(view:View){
        NetWorkUtils.stop()
    }


    fun wifiInfo(view:View){
        val info =  NetWorkUtils
            .getWifiInfo(context)
        if (info!=null){
            val rri = info.bssid
            showToast(rri)
        }
    }


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        NetWorkUtils.addListener(listener)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        NetWorkUtils.removeListener(listener)
    }

    val listener = object : NetWorkUtils.OnNetWorDelayListener{
        override fun onGetLostInfo(lost: String) {

        }

        override fun onGetNetDelay(delay: String) {
            Log.d("1231313===>",delay)
        }

        override fun onError(error: Exception) {

        }

    }


}

fun Map<String, Any>.toJsonMap(): Map<String, String> {
    return mapOf(
        "json" to this.toAccessorJson()
    )
}