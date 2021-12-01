package com.ckz.baselibrary.utils

import android.util.Log
import com.ckz.baselibrary.BuildConfig

object LogUtils {
    private val isDebug = BuildConfig.DEBUG
    @JvmStatic
    fun d(tag: String?, d: String) {
        if (isDebug) {
            Log.d(tag, d)
        }
    }

    @JvmStatic
    fun e(tag: String?, e: String) {
        if (isDebug) {
            Log.e(tag, e)
        }
    }

    @JvmStatic
    fun w(tag: String?, w: String) {
        if (isDebug) {
            Log.w(tag, w)
        }
    }

    @JvmStatic
    fun i(tag: String?, i: String) {
        if (isDebug) {
            Log.i(tag, i)
        }
    }
}