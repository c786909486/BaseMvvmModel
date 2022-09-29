package com.axun.basemvvmmodel

import android.os.Bundle
import android.view.View
import com.axun.basemvvmmodel.databinding.FragmentTestBinding
import com.ckz.baselibrary.base.BaseFragment

/**
 *@packageName com.axun.basemvvmmodel
 *@author kzcai
 *@date 2022/9/28
 */
class TestFragment:BaseFragment<FragmentTestBinding,TestFragmentVIewModel>() {
    override fun initContentView(): Int {
        return R.layout.fragment_test
    }

    override fun initVariableId(): Int {
        return 0
    }

    override fun onRetryBtnClick() {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.requestCreate()
    }
}