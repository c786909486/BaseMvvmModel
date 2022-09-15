package com.axun.library_update.update

import java.io.Serializable

/**
 *@packageName com.axun.library_update.update
 *@author kzcai
 *@date 2022/9/15
 */
abstract class IUpdateModel:Serializable {

    abstract val iNetVersion :String

    abstract val iUrl:String

    abstract val iChangLog:String

}