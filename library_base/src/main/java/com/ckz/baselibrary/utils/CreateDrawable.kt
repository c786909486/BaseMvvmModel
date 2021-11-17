package com.ckz.baselibrary.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

/**
 *@packageName com.axun.ccrccustomerapp.utils
 *@author kzcai
 *@date 2020/11/2
 */
object CreateDrawable {

    fun create(color:Int,strokeColor:Int= Color.parseColor("#ffffff"),width:Int=0,cornerRadius:Float=0f):GradientDrawable{
        val drawble = GradientDrawable()
        drawble.setColor(color)
        drawble.setStroke(width,strokeColor)
        drawble.cornerRadius = cornerRadius
        return drawble
    }
}