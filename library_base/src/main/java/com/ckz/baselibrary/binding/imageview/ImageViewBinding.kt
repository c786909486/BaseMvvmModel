package com.ckz.baselibrary.binding.imageview

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 *@packageName com.ckz.baselibrary.binding.imageview
 *@author kzcai
 *@date 2020/6/17
 */
object ImageViewBinding {
    /**
     * @Description 加载图片
     **/
    @SuppressLint("CheckResult")
    @BindingAdapter("imageUrl","placeHolderId","errorId",requireAll = false)
    @JvmStatic
    fun imageUrl(imageView: ImageView,imageUrl:String,placeHolderId:Int?,errorId:Int?){
        val options = RequestOptions.centerCropTransform()
        if (placeHolderId!=null){
            options.placeholder(placeHolderId)
        }
        if (errorId!=null){
            options.placeholder(errorId)
        }
        Glide.with(imageView.context).load(imageUrl).apply(options).into(imageView)
    }

    /**
     * @Description 加载圆角图片
     **/
    @SuppressLint("CheckResult")
    @BindingAdapter("roundImageUrl","radius","placeHolderId","errorId",requireAll = false)
    fun loadRoundImage(imageView: ImageView,url: String,radius:Int?,placeHolderId:Int?,errorId:Int?){
        val options = RequestOptions.bitmapTransform(RoundedCorners(radius?:0))
        if (placeHolderId!=null){
            options.placeholder(placeHolderId)
        }
        if (errorId!=null){
            options.placeholder(errorId)
        }
        Glide.with(imageView.context).load(url)
            .apply(options)
            .into(imageView)
    }

    /**
     * @Description 加载圆形图片
     **/
    @SuppressLint("CheckResult")
    @BindingAdapter("circleImageUrl","placeHolderId","errorId")
    fun loadCircleImage(imageView: ImageView,url:String,placeHolderId:Int?,errorId:Int?){
        val options = RequestOptions.bitmapTransform(CircleCrop())
        if (placeHolderId!=null){
            options.placeholder(placeHolderId)
        }
        if (errorId!=null){
            options.placeholder(errorId)
        }
        Glide.with(imageView.context).load(url)
            .apply(options)
            .into(imageView)
    }



}