package com.ckz.library_base_compose.base

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mycomposedemo.base.*
import kotlinx.coroutines.channels.Channel

/**
 *@packageName com.example.mycomposedemo.viewmodel
 *@author kzcai
 *@date 2023/2/6
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private var isDialog = MutableLiveData(false)

    private var pageData = MutableLiveData(PageState.CONTENT.bindData())



    @Composable
    fun ContentWidget(
        modifier: Modifier = Modifier,
        onRetry: OnRetry = { retryClick() },
        loading: @Composable (StateLayoutData) -> Unit = { DefaultLoadingLayout(it) },
        empty: @Composable (StateLayoutData) -> Unit = { DefaultEmptyLayout(it) },
        error: @Composable (StateLayoutData) -> Unit = { DefaultErrorLayout(it) },
        content: @Composable () -> Unit = { }
    ) {
        val showDialog = isDialog.observeAsState()

        val pageStateData = pageData.observeAsState()

        DefaultStateLayout(
            modifier = modifier,
            pageStateData = pageStateData.value!!,
            onRetry = onRetry,
            loading = loading,
            empty = empty,
            error = error,
            content = content
        )

        if (true==showDialog.value){
            Dialog(onDismissRequest = {
                isDialog.value = !isDialog.value!!
            }, properties = DialogProperties(dismissOnClickOutside = false)) {
                Column(
                    modifier = Modifier
                        .background(Color.Transparent),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(text = "加载中...")
                }
            }
        }
    }

    fun showContent(){
        pageData.value = PageState.CONTENT.bindData()
    }

    fun showLoading(msg:String = ""){
        val setData = StateData(tipTex = msg)
        pageData.value = PageState.LOADING.bindData(setData)
    }

    fun showEmpty(emptyMsg:String = "暂无数据"){
        val setData = StateData(tipTex = emptyMsg, btnText = "点击重试")
        pageData.value = PageState.EMPTY.bindData(setData)
    }

    fun showError(errorMsg:String){
        val setData = StateData(tipTex = errorMsg, btnText = "点击重试")
        pageData.value = PageState.ERROR.bindData(setData)
    }

    fun showProcessDialog(){
        isDialog.value = true
    }

    fun hideDialog(){
        isDialog.value = false
    }

    fun retryClick(){
        showLoading()
    }
}
