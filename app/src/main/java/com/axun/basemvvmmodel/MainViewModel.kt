package com.axun.basemvvmmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.ckz.baselibrary.base.BaseViewModel
import com.yyt.librarynetwork.HttpManager
import com.yyt.librarynetwork.utils.toAccessorJson
import com.yyt.librarynetwork.utils.toNetError
import com.yyt.librarynetwork.utils.toObject
import kotlinx.coroutines.launch

/**
 *@packageName com.axun.basemvvmmodel
 *@author kzcai
 *@date 2022/6/10
 */
class MainViewModel(application: Application) : BaseViewModel(application) {

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
}

fun Map<String, Any>.toJsonMap(): Map<String, String> {
    return mapOf(
        "json" to this.toAccessorJson()
    )
}