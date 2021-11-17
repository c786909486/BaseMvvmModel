package com.ckz.baselibrary.binding.recyclerview

import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ckz.baselibrary.R
import com.ckz.baselibrary.decoration.DividerSpace
import com.ckz.baselibrary.decoration.GridSpacingItemDecoration
import com.ckz.baselibrary.decoration.HorizontalSpaceDecoration
import com.ckz.baselibrary.utils.DisplayUtils
import com.ckz.baselibrary.utils.DividerLineUtils

/**
 *@packageName com.ckz.baselibrary.binding.recyclerview
 *@author kzcai
 *@date 2020/6/17
 */
object RecyclerViewBinding {
    /**
     * @Description 设置linearLayoutManager  0：HORIZONTAL  1：VERTICAL
     **/
    @BindingAdapter("linearLayoutOrientation")
    @JvmStatic
    fun setLinearLayoutManager(recyclerView: RecyclerView,orientation:Int = 1){
        val or = if (orientation==1) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context,or,false)
    }

    /**
     * @Description 设置gridLayoutManager
     **/
    @BindingAdapter("gridOrientation","count",requireAll = false)
    @JvmStatic
    fun setGridLayoutManager(recyclerView: RecyclerView,orientation:Int = 1,count:Int = 1){
        val or = if (orientation==1) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context,count,or,false)
    }

    /**
     * @Description 设置adapter
     **/
    @BindingAdapter("adapter")
    @JvmStatic
    fun setAdapter(recyclerView:RecyclerView,adapter:BaseQuickAdapter<*,*>){
        recyclerView.adapter = adapter

    }

    /**
     * @Description 设置横向、纵向decoration
     **/
    @BindingAdapter("decorationOrientation","space","beginStart","isReverseLayout",requireAll = false)
    @JvmStatic
    fun setDecorationSpace(recyclerView: RecyclerView,decorationOrientation:Int = 1,space:Int=0,beginStart:Boolean = true,reverseLayout:Boolean=false){
        if (recyclerView.itemDecorationCount>0){
            return
        }
        val size = DisplayUtils.dp2px(recyclerView.context,space.toFloat())
        val decoration =  DividerSpace(size,decorationOrientation==1,beginStart,reverseLayout)
        recyclerView.addItemDecoration(decoration)
    }

    @BindingAdapter("divider")
    @JvmStatic
    fun setDivider(recyclerView: RecyclerView,@DrawableRes divider:Int = R.drawable.diver_line){
        if (recyclerView.itemDecorationCount>0){
            return
        }
        val divider = DividerLineUtils.dividerLine(recyclerView.context,divider)
        recyclerView.addItemDecoration(divider)
    }

    @BindingAdapter("nestScrollEnable")
    @JvmStatic
    fun setNestScrollEnable(recyclerView: RecyclerView,enable:Boolean = true){
        recyclerView.isNestedScrollingEnabled = enable
    }

    /**
     * @Description 设置grid decoration
     **/
    @BindingAdapter("spaceCount","gridSpace","includeEdge")
    @JvmStatic
    fun setGridDecoration(recyclerView: RecyclerView,spaceCount:Int,gridSpace:Int,includeEdge:Boolean){
        if (recyclerView.itemDecorationCount>0){
            return
        }
        val decoration = GridSpacingItemDecoration(spaceCount,gridSpace,includeEdge)
        recyclerView.addItemDecoration(decoration)
    }

    @BindingAdapter("decoration")
    @JvmStatic
    fun setDecoration(recyclerView: RecyclerView,decoration:RecyclerView.ItemDecoration){
        if (recyclerView.itemDecorationCount>0){
            return
        }
        recyclerView.addItemDecoration(decoration)
    }

    @BindingAdapter("snap")
    @JvmStatic
    fun setSnap(recyclerView: RecyclerView,snapHelper: SnapHelper){
        snapHelper.attachToRecyclerView(recyclerView)
    }
}

