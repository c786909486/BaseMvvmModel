package com.axun.library_update.update

import android.os.Bundle
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import com.ckz.library_update.BR
import com.ckz.library_update.R
import com.ckz.library_update.databinding.ActivityUpdateAppBinding
import com.ckz.baselibrary.base.BaseCompatActivity
import com.ckz.baselibrary.utils.ScreenUtils

/**
 *@packageName com.ckz.library_update.update
 *@author kzcai
 *@date 2022/9/15
 */
class UpdateAppActivity:BaseCompatActivity<ActivityUpdateAppBinding, UpdateAppViewModel>() {
    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_update_app
    }

    override fun initVariableId(): Int {
        return BR.updateViewModel
    }

    override fun onRetryBtnClick() {

    }

    private fun setWindow(){
        val params = window.attributes
        val sw = ScreenUtils.getScreenWidth(context)
        val sh = ScreenUtils.getScreenHeight(context)
        if (sh>sw){
            params.width = ScreenUtils.getScreenWidth(context)*2/3
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
        }else{
            params.width = sw/3-40
            params.height = sh
        }
        window.attributes = params
    }

    private fun getActivitySize(){
        binding!!.rlRoot.post {
            val height = binding?.rlRoot?.height?:0
            val llContent = binding!!.llContent
            val params = llContent.layoutParams as RelativeLayout.LayoutParams
            params.topMargin = (height*0.4).toInt()
            llContent.layoutParams = params
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel?.requestCreate()
        setWindow()
        getActivitySize()
//        onBackPressedDispatcher.addCallback(this,object :OnBackPressedCallback(false){
//            override fun handleOnBackPressed() {
//
//            }
//
//        })
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }




}