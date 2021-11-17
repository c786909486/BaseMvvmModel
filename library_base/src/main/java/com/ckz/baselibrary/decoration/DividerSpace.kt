package com.ckz.baselibrary.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author kzcai
 * @packageName com.axun.tqhmanager.ui
 * @date 2020/5/19
 */
class DividerSpace(var space: Int,var isVertical:Boolean = true,var beginStart:Boolean = true,var reverseLayout:Boolean = false) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        var total = state.itemCount
        if (isVertical){
            if (!reverseLayout){
                if (position == 0&&beginStart) {
                    outRect.top = space
                } else {
                    outRect.top = 0
                }
                outRect.bottom = space
            }else{
                if (position == 0&&beginStart) {
                    outRect.bottom = space
                } else {
                    outRect.bottom = 0
                }
                outRect.top = space
            }
        }else{
            if (!reverseLayout){
                if (position == 0 &&beginStart) {
                    outRect.left = space
                } else {
                    outRect.left = 0
                }
                if (position == total-1){
                    outRect.right = 0
                }else{
                    outRect.right = space
                }
            }else{
                if (position == 0 ) {
                    outRect.right = space
                } else {
                    outRect.right = 0
                }
                if (position == total-1 &&beginStart){
                    outRect.left = 0
                }else{
                    outRect.left = space
                }
            }
        }

    }

}