package com.ckz.baselibrary.utils

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.ckz.library_base.R

/**
 *@packageName com.axun.tqhmanager.ui
 *@author kzcai
 *@date 2020/5/19
 */
object DividerLineUtils {

    fun dividerLine(context: Context):DividerItemDecoration{
        return dividerLine(context, R.drawable.diver_line)
    }


    fun dividerLine(context: Context,@DrawableRes resId:Int):DividerItemDecoration{
        val divider = DividerItemDecoration(context,DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(context,resId)!!)
        return divider
    }


}