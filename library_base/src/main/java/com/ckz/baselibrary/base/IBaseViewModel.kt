package com.ckz.baselibrary.base

import androidx.lifecycle.*

/**
 *@packageName com.ckz.baselibrary.base
 *@author kzcai
 *@date 2020/5/11
 */
interface IBaseViewModel: DefaultLifecycleObserver {

    /**
     * 注册RxBus
     */
    fun registerRxBus()

    /**
     * 移除RxBus
     */
    fun removeRxBus()
}