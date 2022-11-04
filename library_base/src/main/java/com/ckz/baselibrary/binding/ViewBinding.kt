package com.ckz.baselibrary.binding

import android.graphics.drawable.Drawable
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 *@packageName com.ckz.baselibrary.binding
 *@author kzcai
 *@date 11/25/20
 */
object ViewBinding {
    @BindingAdapter("bgDrawable")
    @JvmStatic
    fun bgDrawable(view: View, drawable: Drawable){
        view.background = drawable
    }

    @BindingAdapter("isShow")
    @JvmStatic
    fun isShow(view: View,isShow:Boolean){
        view.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    @BindingAdapter("isVisibility")
    @JvmStatic
    fun isVisibility(view: View,isShow:Boolean){
        view.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
    }

    @BindingAdapter("isSelected")
    @JvmStatic
    fun isSelected(view:View,isSelected:Boolean){
        view.isSelected = isSelected
    }

}