package com.ckz.library_base_compose.utils

import android.annotation.SuppressLint
import android.content.Context
import java.lang.Exception

/**
 * @author kzcai
 * @packageName com.ckz.baselibrary.utils
 * @date 2020/5/11
 */
class Utils private constructor() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        /**
         * 初始化工具类
         *
         * @param context 上下文
         */
        fun init(context: Context) {
            Companion.context = context.applicationContext
        }

        /**
         * 获取ApplicationContext
         *
         * @return ApplicationContext
         */
        fun getContext(): Context? {
            if (context != null) {
                return context
            }
            throw NullPointerException("should be initialized in application")
        }

        fun getAppVersion(context: Context): String? {
            var version = "1.0.0" //默认1.0.0版本
            val manager = context.packageManager
            try {
                val info = manager.getPackageInfo(context.packageName, 0)
                version = info.versionName
            } catch (e: Exception) {
            }
            return version
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}