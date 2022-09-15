package com.axun.library_update.update

import android.os.Bundle
import android.view.WindowManager
import com.axun.library_update.BR
import com.axun.library_update.R
import com.axun.library_update.databinding.ActivityUpdateAppBinding
import com.ckz.baselibrary.base.BaseCompatActivity
import com.ckz.baselibrary.utils.ScreenUtils

/**
 *@packageName com.axun.library_update.update
 *@author kzcai
 *@date 2022/9/15
 */
class UpdateAppActivity:BaseCompatActivity<ActivityUpdateAppBinding,UpdateAppViewModel>() {
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
        params.width = ScreenUtils.getScreenWidth(context)*2/3
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel?.activity = this
        viewModel?.requestCreate()
        setWindow()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}