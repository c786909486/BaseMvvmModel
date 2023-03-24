package com.ckz.baselibrary.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.ckz.baselibrary.R
import com.ckz.baselibrary.loadsir.EmptyCallback
import com.ckz.baselibrary.loadsir.ErrorCallback
import com.ckz.baselibrary.loadsir.LoadingCallback
import com.ckz.baselibrary.message.MessageEvent
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *@packageName com.ckz.baselibrary.base
 *@author kzcai
 *@date 2020/6/16
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel?> : Fragment(), IBaseView,
    LifecycleObserver {
    protected var binding: V? = null

    protected var viewModel: VM? = null

    private var viewModelId: Int = 0

    private var dialog: AlertDialog? = null

    private var dialogView: View? = null

    private var tvMsg: TextView? = null

    private var mLoadService: LoadService<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParam()
        binding = DataBindingUtil.inflate(inflater, initContentView(), container, false)
        initDialog()
        initViewDataBinding()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack()
        //页面数据初始化方法
        initInitializationData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        //注册RxBus
        viewModel?.registerRxBus()
    }

    private fun initDialog() {
        dialog = AlertDialog.Builder(context, R.style.LoadDialog).create()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnDismissListener { onLoadingDismiss() }
        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
        tvMsg = dialogView?.findViewById(R.id.tv_msg)
        dialog?.setOnDismissListener {
            onLoadingDismiss()
        }
    }

    /**
     * 注入绑定
     */
    private fun initViewDataBinding() {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包

        viewModelId = initVariableId()
        viewModel = initViewModel()
        if (viewModel == null) {
            val modelClass: Class<ViewModel>
            val type: Type? = javaClass.genericSuperclass
            modelClass = (if (type is ParameterizedType) {
                (type as ParameterizedType?)!!.actualTypeArguments[1]
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                BaseViewModel::class.java
            }) as Class<ViewModel>
            viewModel = createViewModel(modelClass) as VM
        }
        //关联ViewModel
        binding!!.setVariable(viewModelId, viewModel)
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding!!.lifecycleOwner = this
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel!!)


    }


    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun initContentView(): Int

    /**
     * 初始化ViewModel的id
     * @return BR的id
     */
    abstract fun initVariableId(): Int


    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    open fun initViewModel(): VM? {
        return null
    }

    //刷新布局
    open fun refreshLayout() {
        if (viewModel != null) {
            binding!!.setVariable(viewModelId, viewModel)
        }
    }

    //注册ViewModel与View的契约UI回调事件
    protected open fun registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel?.uC?.showDialogEvent?.observe(viewLifecycleOwner,
            Observer<String?> { title -> showProgress(title!!) })
        //加载对话框消失
        viewModel?.uC?.dismissDialogEvent?.observe(viewLifecycleOwner,
            Observer<Void?> { hideProgress() })
        //toast
        viewModel?.uC?.showToastEvent?.observe(viewLifecycleOwner,
            Observer<String?> { msg -> showToast(msg!!) })
        //showContent
        viewModel?.uC?.showContentEvent?.observe(viewLifecycleOwner,
            Observer { showContent() })
        //showLoading
        viewModel?.uC?.showLoadingEvent?.observe(viewLifecycleOwner,
            Observer { showLoading() })
        //showEmpty
        viewModel?.uC?.showEmptyEvent?.observe(viewLifecycleOwner,
            Observer { showEmpty() })

        //showFailure
        viewModel?.uC?.showFailureEvent?.observe(viewLifecycleOwner,
            Observer<String> { msg -> showFailure(msg) })
    }


    override fun showProgress(msg: String?) {
        if (dialog == null) {
            initDialog()
        }
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
        dialog?.show()
        dialog?.setContentView(dialogView!!)
        tvMsg?.text = msg
    }

    override fun hideProgress() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }


    override fun onLoadingDismiss() {

    }

    /**
     * 注册LoadSir
     *
     * @param view 替换视图
     */
    open fun setLoadSir(view: View?) {
        if (mLoadService == null) {
            Log.d("BaseFragment", "setLoadSir: ")
            mLoadService = LoadSir.getDefault()
                .register(view
                ) { onRetryBtnClick() }
        }
    }

    override fun showContent() {
        if (null != mLoadService) {
            mLoadService?.apply { showSuccess() }
        }
    }

    override fun showLoading() {
        if (null != mLoadService) {
            mLoadService?.apply { showCallback(LoadingCallback::class.java) }
        }
    }

    override fun showEmpty() {
        if (null != mLoadService) {
            mLoadService?.apply { showCallback(EmptyCallback::class.java) }
        }
    }

    override fun showFailure(message: String?) {
        if (null != mLoadService) {

            mLoadService?.apply { showCallback(ErrorCallback::class.java) }

        }
    }


    override fun initParam() {

    }

    override fun initInitializationData() {

    }

    override fun initViewObservable() {

    }

    open fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
    </T> */
    open fun <T : ViewModel> createViewModel(
        cls: Class<T>
    ): T {
        return ViewModelProvider(this)[cls]
    }


    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    open fun hideSoftKeyboard(
        context: Context,
        viewList: List<View>?
    ) {
        if (viewList == null) return
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        for (v in viewList) {
            inputMethodManager.hideSoftInputFromWindow(
                v.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    /**
     * 失败重试,重新加载事件
     */
    protected abstract fun onRetryBtnClick()

    override fun onDestroy() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
        dialog = null
        viewModel?.removeRxBus()
        mLoadService = null
        binding = null
        viewModel = null
        tvMsg = null
        super.onDestroy()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: MessageEvent) {
        viewModel?.receiveEvent(event)
    }

}