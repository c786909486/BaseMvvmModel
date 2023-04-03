package com.ckz.baselibrary.base

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import com.ckz.baselibrary.R
import com.ckz.baselibrary.loadsir.EmptyCallback
import com.ckz.baselibrary.loadsir.ErrorCallback
import com.ckz.baselibrary.loadsir.LoadingCallback
import com.ckz.baselibrary.message.MessageEvent
import com.ckz.baselibrary.utils.LogUtils
import com.kingja.loadsir.callback.Callback.OnReloadListener
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 *@packageName com.ckz.baselibrary.base
 *@author kzcai
 *@date 2020/5/11
 */

abstract class BaseCompatActivity<V:ViewDataBinding,VM : BaseViewModel?>: AppCompatActivity(),IBaseView,LifecycleObserver {

    protected var binding:V?=null

    protected var viewModel: VM? = null

    private var viewModelId:Int = 0

    private var dialog:AlertDialog?=null

    private var dialogView: View?=null

    private var tvMsg:TextView?=null

    private var mLoadService: LoadService<*>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d("currentActivity===>",this.localClassName)
        //页面接受的参数方法
        initParam()
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState)
        initDialog()
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack()
        //页面数据初始化方法
        initInitializationData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        //注册RxBus
//        viewModel?.registerRxBus()

    }

    private fun initDialog(){
        dialog = AlertDialog.Builder(this, R.style.LoadDialog).create()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnDismissListener(DialogInterface.OnDismissListener { onLoadingDismiss() })
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_progress, null)
        tvMsg = dialogView?.findViewById(R.id.tv_msg)
        dialog?.setOnDismissListener {
            onLoadingDismiss()
        }
    }

    /**
     * 注入绑定
     */
    private  fun initViewDataBinding(savedInstanceState: Bundle?) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))
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
            viewModel = createViewModel( modelClass) as VM
        }
        //关联ViewModel
        binding!!.setVariable(viewModelId, viewModel)
        //支持LiveData绑定xml，数据改变，UI自动会更新
        binding!!.lifecycleOwner = this
        //让ViewModel拥有View的生命周期感应
        viewModel!!.initActivity(this)
        lifecycle.addObserver(viewModel!!)
    }





    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun initContentView(savedInstanceState: Bundle?): Int

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
        viewModel?.uC?.showDialogEvent?.observe(this,
            Observer<String?> { title ->
                showProgress(title!!)
            })
        //加载对话框消失
        viewModel?.uC?.dismissDialogEvent?.observe(this,
            Observer<Void?> { hideProgress() })
        //toast
        viewModel?.uC?.showToastEvent?.observe(this,
            Observer<String?> { msg -> showToast(msg!!) })
        //showContent
        viewModel?.uC?.showContentEvent?.observe(this,
            Observer { showContent() })
        //showLoading
        viewModel?.uC?.showLoadingEvent?.observe(this,
            Observer { showLoading() })
        //showEmpty
        viewModel?.uC?.showEmptyEvent?.observe(this,
            Observer { showEmpty() })

        //showFailure
        viewModel?.uC?.showFailureEvent?.observe(this,
            Observer<String> {msg-> showFailure(msg) })
    }


    override fun showProgress(msg: String?) {
        if (dialog==null){
            initDialog()

        }
        if (dialog?.isShowing==true){
            dialog?.dismiss()
        }
        dialog?.show()
        dialog?.setContentView(dialogView!!)
        tvMsg?.text = msg
    }

    override fun hideProgress() {
        if (dialog!=null&&dialog!!.isShowing){
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
            mLoadService = LoadSir.getDefault()
                .register(view,
                    OnReloadListener { v: View? -> onRetryBtnClick() }
                )
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

    val context:Context get() = this

    override fun initParam() {

    }

    override fun initInitializationData() {

    }

    override fun initViewObservable() {

    }

    open fun showToast(msg:String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
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

    open fun logout() {
        AppManager.appManager?.finishAllActivity()
    }


    /**
     * 软键盘显示/隐藏
     */
    open fun hideShowKeyboard() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager //得到InputMethodManager的实例
        if (imm.isActive) { //如果开启
            imm.toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                InputMethodManager.HIDE_NOT_ALWAYS
            ) //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    open fun hideSoftKeyboard(activity: Activity) {
        val view: View? = activity.currentFocus
        if (view != null) {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
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
       if (dialog?.isShowing==true){
           dialog?.dismiss()
       }
        binding = null
        dialog = null
        viewModel = null
        mLoadService = null
        dialogView = null
        tvMsg = null
        super.onDestroy()

    }

}