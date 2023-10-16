package com.axun.library_update.update

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.ckz.baselibrary.utils.VersionCompare
import java.lang.Exception

/**
 *@packageName com.ckz.library_update.update
 *@author kzcai
 *@date 2022/9/15
 */
class UpdateUtils {

    companion object{
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
            UpdateUtils()
        }
    }

    private var context:Context?=null
    private var showToast = false

    fun init(context: Context){
        this.context = context
    }

    fun getAppVersion(context: Context): String {
        var version = "1.0.0" //默认1.0.0版本
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            version = info.versionName
        } catch (e: Exception) {
        }
        return version
    }


    fun<T: IUpdateModel> checkUpdate(netData:T?, showToast:Boolean = false){
        this.showToast = showToast
        if (netData==null){
            toast("未检测到更新版本")
            return
        }

        val version = netData.iNetVersion
        val localVersion = getAppVersion(context!!)
        if(VersionCompare.compareVersion(version,localVersion)==1){
            val intent = Intent(context, UpdateAppActivity::class.java)
            intent.putExtra("data",netData)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startActivity(intent)
        }else{
            toast("当前是最新版本")
        }

    }


    private fun toast(msg:String){
        if (!showToast){
            return
        }
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}