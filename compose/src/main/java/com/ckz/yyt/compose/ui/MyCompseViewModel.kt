package com.ckz.yyt.compose.ui

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.ckz.library_base_compose.base.BaseViewModel

/**
 *@packageName com.axun.basemvvmmodel
 *@author kzcai
 *@date 2023/10/15
 */
class MyCompseViewModel(application: Application):BaseViewModel(application) {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        showToast("onCreate")
    }
}