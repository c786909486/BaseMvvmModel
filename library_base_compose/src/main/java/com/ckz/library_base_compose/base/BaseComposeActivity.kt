package com.ckz.library_base_compose.base

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.ckz.library_base_compose.utils.ComposeToastUtils

/**
 *@packageName com.ckz.library_base_compose.base
 *@author kzcai
 *@date 2023/10/16
 */
abstract class BaseComposeActivity:ComponentActivity(),LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComposeToastUtils.toastData.observe(this,toastObserver)
        setContent {
            initPage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ComposeToastUtils.toastData.removeObservers(this)
    }

    private val toastObserver = Observer<String> {
        Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
    }

    @Composable
    abstract fun initPage()
}