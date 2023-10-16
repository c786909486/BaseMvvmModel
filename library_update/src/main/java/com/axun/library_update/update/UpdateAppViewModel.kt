package com.axun.library_update.update

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ckz.baselibrary.base.BaseViewModel
import com.yyt.librarynetwork.HttpManager
import com.yyt.librarynetwork.listener.OnDownloadListener
import kotlinx.coroutines.launch
import java.io.File

/**
 *@packageName com.ckz.library_update.update
 *@author kzcai
 *@date 2022/9/15
 */
class UpdateAppViewModel(application: Application):BaseViewModel(application) {

    lateinit var appData: IUpdateModel
    var nerVersion = MutableLiveData("")
    var changeLog = MutableLiveData("")
    var inDownload = MutableLiveData(false)
    var percent = MutableLiveData(0)

    fun requestCreate(){
        appData = activity?.intent?.getSerializableExtra("data") as IUpdateModel
        nerVersion.value = "v${appData.iNetVersion}"
        changeLog.value = appData.iChangLog.ifEmpty { "修复已知bug" }
    }

    fun close(view: View){
        activity?.finish()
    }

    fun downloadApp(view:View){
        viewModelScope.launch {
            val path = pageContext!!.getExternalFilesDir(null)!!.absolutePath
            val url = appData.iUrl
            val name = url.substring(url.lastIndexOf("/"),url.length)
            val filePath = path + File.separator + name
            HttpManager.instance.downloadFile(appData.iUrl,filePath,object : OnDownloadListener {
                override fun onDownloadFail(error: String) {
                    showToast("下载失败")
                    activity?.finish()
                }

                override fun onDownloadFinish() {


                    val file = File(filePath)
                    if (file.length() / 1024 / 1024 < 1) {
                    showToast("下载失败")
                    activity?.finish()
                    } else {
                        InstallAppUtils.installApp((pageContext as Activity), file)
                        activity?.finish()
                    }
                }

                override fun onDownloadStart() {
                    inDownload.value = true
                }

                override fun onDownloading(currentPercent: Int, currentSize: Long, totalSize: Long) {
                    percent.value = currentPercent
                }

            })
        }
    }
}