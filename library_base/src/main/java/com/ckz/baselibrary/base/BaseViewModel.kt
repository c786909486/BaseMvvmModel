package com.ckz.baselibrary.base

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.ckz.baselibrary.bus.SingleLiveEvent
import com.ckz.baselibrary.loadsir.EmptyCallback
import com.ckz.baselibrary.loadsir.ErrorCallback
import com.ckz.baselibrary.loadsir.LoadingCallback
import com.ckz.baselibrary.message.MessageEvent
import com.ckz.baselibrary.utils.LogUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author kzcai
 * @packageName com.ckz.baselibrary.base
 * @date 2020/5/11
 */
open class BaseViewModel @JvmOverloads constructor(
    application: Application

) : AndroidViewModel(application), IBaseViewModel,LifecycleOwner {
    private var uc: UIChangeLiveData? = null
    private val TAG = this.javaClass.simpleName

    /**
     * 保存使用注解的 model ，用于解绑
     */
    private var mInjectModels: MutableList<BaseModel?> = ArrayList()

    var context = application.applicationContext

    var activity:AppCompatActivity?=null

    var fragment : Fragment?=null

    val pageContext get() = when {
        activity!=null -> {

            activity as Context
        }
        fragment!=null -> {
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
                    throw  RuntimeException("SubClass must extends Class:BaseModel");
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

    fun showDialog(title: String = "") {
        uc!!.showDialogEvent!!.postValue(title)
    }

    fun dismissDialog() {
        uc!!.dismissDialogEvent!!.call()
    }


    fun showToast(msg: String = "") {
        uc!!.showToastEvent!!.postValue(msg)
    }

    fun showContent() {
        uc!!.showContentEvent!!.call()
    }

    fun showLoading() {
        uc!!.showLoadingEvent!!.call()
    }

    fun showEmpty() {
        uc!!.showEmptyEvent!!.call()
    }

    fun showFailure(message: String?) {
        uc!!.showFailureEvent!!.postValue(message)
    }

    fun startActivity(intent:Intent,options:Bundle?){
        when {
            activity!=null -> {
                activity!!.startActivity(intent,options)
            }
            fragment!=null -> {
                fragment!!.startActivity(intent,options)
            }
            else -> {
                throw (Exception("activity or fragment is null"))
            }
        }
    }

    fun startActivity(intent: Intent){
        startActivity(intent,null)
    }

    fun createLauncher(callback:(resultCode:Int,result:ActivityResult)->Unit):ActivityResultLauncher<Intent>{
        return when{
            activity!=null->{
                activity!!.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                    callback.invoke(it.resultCode,it)
                }
            }
            fragment!=null->{
                fragment!!.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                    callback.invoke(it.resultCode,it)
                }
            }
            else->{
                throw (Exception("activity or fragment is null"))
            }
        }

    }

    fun receiveEvent(event: MessageEvent){

    }


    override fun onAny(
        owner: LifecycleOwner?,
        event: Lifecycle.Event?
    ) {
    }

    override fun onCreate() {
        LogUtils.d(TAG, "onCreate")
    }

    override fun onDestroy() {
        LogUtils.d(TAG, "onDestroy")

    }

    override fun onStart() {
        LogUtils.d(TAG, "onStart")

    }

    override fun onStop() {
        LogUtils.d(TAG, "onStop")

    }

    override fun onResume() {
        LogUtils.d(TAG, "onResume")

    }

    override fun onPause() {
        LogUtils.d(TAG, "onPause")

    }

    override fun registerRxBus() {
        EventBus.getDefault().register(this)
    }

    override fun removeRxBus() {
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event:MessageEvent){

    }


    override fun onCleared() {
        super.onCleared()
        for (model in mInjectModels) {
            model?.onCleared()
        }
    }

    override fun getLifecycle(): Lifecycle {
        return when {
            activity!=null -> {
                activity!!.lifecycle
            }
            fragment!=null -> {
                fragment!!.lifecycle
            }
            else -> {
                activity!!.lifecycle
            }
        }
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

    var showEmptyEvent:SingleLiveEvent<Void>?=null
        get() = createLiveData(field).also { field = it }
        private set

    var showFailureEvent:SingleLiveEvent<String>?=null
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