package com.ckz.library_base_compose.base

import androidx.lifecycle.*

/**
 *@packageName com.ckz.baselibrary.base
 *@author kzcai
 *@date 2020/5/11
 */
interface IBaseViewModel {


    fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?)

    fun onCreate(owner: LifecycleOwner)

    fun onDestroy(owner: LifecycleOwner)

    fun onStart(owner: LifecycleOwner)

    fun onStop(owner: LifecycleOwner)

    fun onResume(owner: LifecycleOwner)

    fun onPause(owner: LifecycleOwner)

    /**
     * 注册RxBus
     */
    fun registerRxBus()

    /**
     * 移除RxBus
     */
    fun removeRxBus()
}