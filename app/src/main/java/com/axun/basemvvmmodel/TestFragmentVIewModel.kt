package com.axun.basemvvmmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.ckz.baselibrary.base.BaseViewModel
import kotlinx.coroutines.launch

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
}