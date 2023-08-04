package com.axun.basemvvmmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.viewModelScope
import com.ckz.baselibrary.base.BaseViewModel
import com.ckz.baselibrary.message.MessageEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 *@packageName com.axun.basemvvmmodel
 *@author kzcai
 *@date 2022/9/28
 */
class TestFragmentVIewModel(application: Application):BaseViewModel(application) {

    val model = TestModel()
    fun requestCreate(){
        viewModelScope.launch {
            val data = model.getData()
            showToast(data)
        }
    }


    fun sendToast(view: View){
        EventBus.getDefault().post(MessageEvent(111))
    }

    override fun onMessageEvent(event: MessageEvent) {
        super.onMessageEvent(event)
        showToast(event.code.toString())
    }
}