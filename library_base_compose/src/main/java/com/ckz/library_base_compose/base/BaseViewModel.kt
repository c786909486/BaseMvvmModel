package com.ckz.library_base_compose.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ckz.baselibrary.message.MessageEvent
import com.ckz.library_base_compose.utils.ComposeToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *@packageName com.example.mycomposedemo.viewmodel
 *@author kzcai
 *@date 2023/2/6
 */
abstract class BaseViewModel() : ViewModel(),
    IBaseViewModel {

    var isDialog by mutableStateOf(false)

    var dialogText by mutableStateOf("")

    var canDismissByTouch by mutableStateOf(false)

    var canDismissByBack by mutableStateOf(true)

    var pageData by mutableStateOf(PageState.CONTENT.bindData())

    open val unregisterOnStop get() = false

    var hasCreated = false

    /**
     * 保存使用注解的 model ，用于解绑
     */
    private var mInjectModels: MutableList<BaseModel?> = ArrayList()


    fun showContent() {
        pageData = PageState.CONTENT.bindData()
    }

    fun showLoading(msg: String = "") {
        val setData = StateData(tipTex = msg)
        pageData = PageState.LOADING.bindData(setData)
    }

    fun showEmpty(emptyMsg: String = "暂无数据") {
        val setData = StateData(tipTex = emptyMsg, btnText = "点击重试")
        pageData = PageState.EMPTY.bindData(setData)
    }

    fun showError(errorMsg: String) {
        val setData = StateData(tipTex = errorMsg, btnText = "点击重试")
        pageData = PageState.ERROR.bindData(setData)
    }

    fun showProcessDialog(
        msg: String = "加载中...",
        canTouchDismiss: Boolean = false,
        canTouchByBack: Boolean = true
    ) {
        isDialog = true
        dialogText = msg
        canDismissByTouch = canTouchDismiss
        canDismissByBack = canTouchByBack
    }

    fun hideDialog() {
        isDialog = false
    }

    open fun retryClick() {
//        showLoading()
    }

    open fun showToast(msg: String?) {
        if (!msg.isNullOrEmpty()) {
            ComposeToastUtils.toastData.postValue(msg)
        }

    }


    override fun registerRxBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun removeRxBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: MessageEvent) {

    }


    override fun onCleared() {
        super.onCleared()
        if (!unregisterOnStop) {
            removeRxBus()
        }
    }

    override fun onStart(owner: LifecycleOwner) {

    }

    override fun onStop(owner: LifecycleOwner) {
        if (unregisterOnStop) {
            removeRxBus()
        }
    }

    override fun onCreate(owner: LifecycleOwner) {

    }

    override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {

    }

    override fun onPause(owner: LifecycleOwner) {

    }

    override fun onResume(owner: LifecycleOwner) {


    }

    override fun onDestroy(owner: LifecycleOwner) {

    }

}
