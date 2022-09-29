package com.axun.basemvvmmodel

import com.ckz.baselibrary.base.BaseModel

/**
 *@packageName com.axun.basemvvmmodel
 *@author kzcai
 *@date 2022/9/28
 */
class TestModel:BaseModel() {

    suspend fun getData():String{
        return "1231231"
    }
}