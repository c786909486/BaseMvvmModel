package com.ckz.library_base_compose.base

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
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
abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    IBaseViewModel {

    private var isDialog = MutableLiveData(false)

    var dialogText by mutableStateOf("")

    var canDismissByTouch by mutableStateOf(false)

    var canDismissByBack by mutableStateOf(true)

    private var pageData = MutableLiveData(PageState.CONTENT.bindData())
    open val unregisterOnStop get() = false

    private var hasCreated = false

    /**
     * 保存使用注解的 model ，用于解绑
     */
    private var mInjectModels: MutableList<BaseModel?> = ArrayList()

    init {
        initModel()
    }

    private fun initModel() {
        val fields = this.javaClass.declaredFields
        for (field in fields) {
            val injectModel = field.getAnnotation(InjectModel::class.java)
            if (injectModel != null) {
                try {
                    val type: Class<out BaseModel?> =
                        field.type as Class<out BaseModel?>
                    val mInjectModel = type.newInstance()
                    field.isAccessible = true
                    field.set(this, mInjectModel)
                    mInjectModels.add(mInjectModel)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace();
                } catch (e: InstantiationException) {
                    e.printStackTrace();
                } catch (e: ClassCastException) {
                    e.printStackTrace()
                    throw RuntimeException("SubClass must extends Class:BaseModel");
                }
            }
        }
    }

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
        val lifecycleOwner = LocalLifecycleOwner.current
        val lifecycle = lifecycleOwner.lifecycle

        val observer = remember {
            LifecycleEventObserver { owner, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        if (!hasCreated) {
                            onCreate(owner)
                            hasCreated = true
//                            Log.d("HomePage", "HomePage: ${event.name}")
                        }

                    }

                    Lifecycle.Event.ON_START -> {
                        onStart(owner)
//                        Log.d("HomePage", "HomePage: ${event.name}")
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        onResume(owner)
//                        Log.d("HomePage", "HomePage: ${event.name}")
                    }

                    Lifecycle.Event.ON_PAUSE -> {
//                        onPause(owner)
                        Log.d("HomePage", "HomePage: ${event.name}")
                    }

                    Lifecycle.Event.ON_STOP -> {
                        onStop(owner)
//                        Log.d("HomePage", "HomePage: ${event.name}")
                    }

                    Lifecycle.Event.ON_DESTROY -> {
                        onDestroy(owner)
                        onCleared()
//                        Log.d("HomePage", "HomePage: ${event.name}")
                        hasCreated = false
                    }

                    Lifecycle.Event.ON_ANY -> {
                        onAny(owner, event)
//                        Log.d("HomePage", "HomePage: ${event.name}")
                    }
                }

            }
        }

        DisposableEffect(lifecycleOwner, observer) {
            lifecycle.addObserver(observer)
            onDispose {
                lifecycle.removeObserver(observer)
            }
        }

        DefaultStateLayout(
            modifier = modifier,
            pageStateData = pageStateData.value!!,
            onRetry = onRetry,
            loading = loading,
            empty = empty,
            error = error,
            content = content
        )

        if (true == showDialog.value) {
            Dialog(
                onDismissRequest = {
                    isDialog.value = !isDialog.value!!
                },
                properties = DialogProperties(
                    dismissOnClickOutside = canDismissByTouch,
                    usePlatformDefaultWidth = false,
                    decorFitsSystemWindows = false,
                    dismissOnBackPress = canDismissByBack//
                )
            ) {
                Column(
                    modifier = Modifier

                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color.White)
                    Box(modifier = Modifier.height(10.dp))
                    Text(text = dialogText, color = Color.White)
                }

            }
        }
    }

    fun showContent() {
        pageData.value = PageState.CONTENT.bindData()
    }

    fun showLoading(msg: String = "") {
        val setData = StateData(tipTex = msg)
        pageData.value = PageState.LOADING.bindData(setData)
    }

    fun showEmpty(emptyMsg: String = "暂无数据") {
        val setData = StateData(tipTex = emptyMsg, btnText = "点击重试")
        pageData.value = PageState.EMPTY.bindData(setData)
    }

    fun showError(errorMsg: String) {
        val setData = StateData(tipTex = errorMsg, btnText = "点击重试")
        pageData.value = PageState.ERROR.bindData(setData)
    }

    fun showProcessDialog(
        msg: String = "加载中...",
        canTouchDismiss: Boolean = false,
        canTouchByBack: Boolean = true
    ) {
        isDialog.value = true
        dialogText = msg
        canDismissByTouch = canTouchDismiss
        canDismissByBack = canTouchByBack
    }

    fun hideDialog() {
        isDialog.value = false
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

    override fun onDestroy(owner: LifecycleOwner) {
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

}
