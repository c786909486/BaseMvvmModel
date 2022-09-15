package com.axun.basemvvmmodel

import com.axun.library_update.update.IUpdateModel

/**
 *@packageName com.axun.basemvvmmodel
 *@author kzcai
 *@date 2022/9/15
 */
class AppUpdateData(val version:String,val url:String):IUpdateModel() {
    override val iNetVersion: String
        get() = version

    override val iUrl: String
        get() = url

    override val iChangLog: String
        get() = ""
}