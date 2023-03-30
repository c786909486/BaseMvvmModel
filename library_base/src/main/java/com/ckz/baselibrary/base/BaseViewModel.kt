package com.ckz.baselibrary.base

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.ckz.baselibrary.bus.SingleLiveEvent
import com.ckz.baselibrary.message.MessageEvent
import com.ckz.baselibrary.utils.LogUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

/**
 * @author kzcai
 * @packageName com.ckz.baselibrary.base
 * @date 2020/5/11
 */
open class BaseViewModel @JvmOverloads constructor(
    application: Application

) : AndroidViewModel(application), IBaseViewModel {
    private var uc: UIChangeLiveData? = null
    private val TAG = this.javaClass.simpleName

    /**
     * 保存使用注解的 model ，用于解绑
     */
    private var mInjectModels: MutableList<BaseModel?> = ArrayList()

    val context: Context get() = getApplication<Application>().applicationContext

    private var activityWeak: WeakReference<AppCompatActivity>? = null

    private var fragmentWeak: WeakReference<Fragment>? = null

    val activity:AppCompatActivity? get() = activityWeak?.get()

    val fragment:Fragment? get() = fragmentWeak?.get()

    fun initActivity(activity: AppCompatActivity) {
        this.activityWeak = WeakReference(activity)
    }

    fun initFragment(fragment: Fragment) {
        this.fragmentWeak = WeakReference(fragment)
    }

    val pageContext
        get() = when {
            activity != null -> {

                activity!! as Context
            }
            fragment != null -> {
                fragment!!.context
            }
            else -> {
                throw (Exception("activity or fragment is null"))
            }
        }

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


    val uC: UIChangeLiveData?
        get() {
            if (uc == null) {
                uc = UIChangeLiveData()
            }
            return uc
        }

    open fun showDialog(title: String = "") {
        uc?.showDialogEvent?.postValue(title)
    }

    open fun dismissDialog() {
        uc?.dismissDialogEvent?.call()
    }


    open fun showToast(msg: String = "") {
        uc?.showToastEvent?.postValue(msg)
    }

    open fun showContent() {
        uc?.showContentEvent?.call()
    }

    open fun showLoading() {
        uc?.showLoadingEvent?.call()
    }

    open fun showEmpty() {
        uc?.showEmptyEvent?.call()
    }

    open fun showFailure(message: String?) {
        uc?.showFailureEvent?.postValue(message)
    }

    open fun startActivity(intent: Intent, options: Bundle?) {
        when {
            activity != null -> {
                activity?.startActivity(intent, options)
            }
            fragment != null -> {
                fragment?.startActivity(intent, options)
            }
            else -> {
                throw (Exception("activity or fragment is null"))
            }
        }
    }

    open fun startActivity(intent: Intent) {
        startActivity(intent, null)
    }

    open fun createLauncher(callback: (resultCode: Int, result: ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
        return when {
            activity != null -> {
                activity!!
                    .registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        callback.invoke(it.resultCode, it)
                    }
            }
            fragment != null -> {
                fragment!!
                    .registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                        callback.invoke(it.resultCode, it)
                    }
            }
            else -> {
                throw (Exception("activity or fragment is null"))
            }
        }

    }

    fun receiveEvent(event: MessageEvent) {

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
        for (model in mInjectModels) {
            model?.onCleared()
        }
        mInjectModels.clear()
        uc = null
        activityWeak = null
        fragmentWeak = null

    }

}

class UIChangeLiveData : SingleLiveEvent<Any?>() {
    var showDialogEvent: SingleLiveEvent<String>? = null
        get() = createLiveData(field).also { field = it }
        private set
    var dismissDialogEvent: SingleLiveEvent<Void>? = null
        get() = createLiveData(field).also {
            field = it
        }
        private set

    var showToastEvent: SingleLiveEvent<String>? = null
        get() = createLiveData(field).also { field = it }
        private set

    var showContentEvent: SingleLiveEvent<Void>? = null
        get() = createLiveData(field).also { field = it }
        private set

    var showLoadingEvent: SingleLiveEvent<Void>? = null
        get() = createLiveData(field).also { field = it }
        private set

    var showEmptyEvent: SingleLiveEvent<Void>? = null
        get() = createLiveData(field).also { field = it }
        private set

    var showFailureEvent: SingleLiveEvent<String>? = null
        get() = createLiveData(field).also { field = it }
        private set

    companion object {
        fun <T> createLiveData(liveData: SingleLiveEvent<T>?): SingleLiveEvent<T> {
            var liveData = liveData
            if (liveData == null) {
                liveData = SingleLiveEvent()
            }
            return liveData
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in Any?>) {
        super.observe(owner, observer)
    }

}