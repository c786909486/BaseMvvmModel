package com.ckz.baselibrary.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.ckz.baselibrary.base.AppManager.Companion.appManager
import com.ckz.baselibrary.loadsir.EmptyCallback
import com.ckz.baselibrary.loadsir.ErrorCallback
import com.ckz.baselibrary.loadsir.LoadingCallback
import com.ckz.baselibrary.loadsir.TimeoutCallback
import com.ckz.baselibrary.utils.Utils.Companion.init
import com.kingja.loadsir.core.LoadSir

/**
 * Created by goldze on 2017/6/15.
 */
open class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setApplication(this)
        setLoadSir()
    }

    open fun setLoadSir(){
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())
            .addCallback(LoadingCallback())
            .addCallback(EmptyCallback())
            .addCallback(TimeoutCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }

    companion object {
        private var sInstance: Application? = null

        /**
         * 当主工程没有继承BaseApplication时，可以使用setApplication方法初始化BaseApplication
         *
         * @param application
         */
        @Synchronized
        fun setApplication(application: Application) {
            sInstance = application
            //初始化工具类
            init(application)
            //注册监听每个activity的生命周期,便于堆栈式管理
            application.registerActivityLifecycleCallbacks(object :
                ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) {
                    appManager!!.addActivity(activity)
                }

                override fun onActivityStarted(activity: Activity) {}
                override fun onActivityResumed(activity: Activity) {}
                override fun onActivityPaused(activity: Activity) {}
                override fun onActivityStopped(activity: Activity) {}
                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle
                ) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    appManager!!.removeActivity(activity)
                }
            })
        }

        /**
         * 获得当前app运行的Application
         */
        val instance: Application?
            get() {
                if (sInstance == null) {
                    throw NullPointerException("please inherit BaseApplication or call setApplication.")
                }
                return sInstance
            }
    }
}