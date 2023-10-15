package com.ckz.library_base_compose.base

/**
 *@packageName com.ckz.baselibrary.base
 *@author kzcai
 *@date 2020/5/11
 */
interface IModel {
    /**
     * ViewModel销毁时清除Model，与ViewModel共消亡。Model层同样不能持有长生命周期对象
     */
    fun onCleared()
}