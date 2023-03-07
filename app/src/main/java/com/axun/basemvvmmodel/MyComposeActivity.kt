package com.axun.basemvvmmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/**
 *@packageName com.axun.basemvvmmodel
 *@author kzcai
 *@date 2023/2/3
 */
class MyComposeActivity:ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
}