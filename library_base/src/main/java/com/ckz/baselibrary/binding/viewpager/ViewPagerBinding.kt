package com.ckz.baselibrary.binding.viewpager

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 *@packageName com.ckz.baselibrary.binding.viewpager
 *@author kzcai
 *@date 2020/6/17
 */
object ViewPagerBinding {

    @BindingAdapter("pagerAdapter")
    fun setAdapter(viewPager: ViewPager,pagerAdapter: PagerAdapter){
        viewPager.adapter = pagerAdapter
    }
}