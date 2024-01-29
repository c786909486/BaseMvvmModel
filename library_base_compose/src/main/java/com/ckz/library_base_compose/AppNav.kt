package com.ckz.library_base_compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost


/**
 *@packageName com.ckz.yyt.callnumberscreen
 *@author kzcai
 *@date 2023/11/14
 */
class AppNav {
    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AppNav()
        }
    }

    private var _appNavController:NavHostController?=null

    val navController get() = _appNavController!!

    @Composable
    fun AppHost(
        navController: NavHostController,
        startDestination: String,
        modifier: Modifier = Modifier,
        route: String? = null,
        builder: NavGraphBuilder.() -> Unit
    ) {
        _appNavController = navController
        NavHost(
            navController,
            startDestination,
            modifier,
            route,
            builder
        )
    }
}