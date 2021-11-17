package com.ckz.baselibrary.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *@packageName com.axun.baselib.decoration
 *@author kzcai
 *@date 2020/5/29
 */
class HorizontalSpaceDecoration(private val space:Int): RecyclerView.ItemDecoration()  {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val count = parent.childCount
        if (position==0){
            outRect.left = space
            outRect.right = space
        }else{
            outRect.left = 0
            outRect.right = space
        }
    }
}