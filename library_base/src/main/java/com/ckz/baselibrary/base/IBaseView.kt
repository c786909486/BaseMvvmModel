package com.ckz.baselibrary.base

import android.content.Context

/**
 * Created by ckz on 2018/4/23.
 */
interface IBaseView {
    /**
     * 显示加载弹窗
     * @param msg 文本内容
     */
    fun showProgress(msg: String?)

    /**
     * 关闭加载弹窗
     */
    fun hideProgress()

    /**
     * 加载弹窗关闭时操作
     */
    fun onLoadingDismiss()


    /**
     * 初始化界面传递参数
     */
    fun initParam()

    /**
     * 初始化数据
     */
    fun initInitializationData()

    /**
     * 初始化界面观察者的监听
     */
    fun initViewObservable()

    /**
     * 显示内容
     */
    fun showContent()

    /**
     * 显示加载提示
     */
    fun showLoading()

    /**
     * 显示空页面
     */
    fun showEmpty()

    /**
     * 刷新失败
     */
    fun showFailure(message: String?)

}