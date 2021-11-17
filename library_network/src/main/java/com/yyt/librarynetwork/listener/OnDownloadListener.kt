package com.yyt.librarynetwork.listener

/**
 *@packageName com.yyt.librarynetwork.listener
 *@author kzcai
 *@date 2020/6/16
 */
interface OnDownloadListener {

    fun onDownloadStart()

    fun onDownloading(currentPercent:Int,currentSize:Long,totalSize:Long)

    fun onDownloadFinish()

    fun onDownloadFail(error:String)
}