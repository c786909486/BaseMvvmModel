package com.axun.basemvvmmodel

import android.os.Bundle
import com.axun.basemvvmmodel.databinding.ActivityMainBinding
import com.axun.library_update.update.UpdateUtils
import com.ckz.baselibrary.base.BaseCompatActivity
import com.yyt.librarynetwork.HttpManager

class MainActivity : BaseCompatActivity<ActivityMainBinding,MainViewModel>() {


    override fun initContentView(savedInstanceState: Bundle?): Int {
        return  R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return BR.mainViewModel
    }

    override fun onRetryBtnClick() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UpdateUtils.instance
            .init(context)
        requestPermission()
        HttpManager.instance.apply {
            setReadTimeOut(5*1000L)
            setWriteTimeOut(5*1000L)
            setConnectTimeOut(5*1000L)
            if (BuildConfig.DEBUG){
                setProxy("192.168.3.17",8888)
//                setProxy("192.168.31.186",8888)
//                setProxy("192.168.124.6",8888)
            }
            setBaseUrl("http://ageds.rkph.com.cn:8081/")
            build()
        }
    }


    private fun requestPermission(){
        requestPermissions(
            arrayOf(android.Manifest.permission.READ_PHONE_STATE),200
        )
    }
}