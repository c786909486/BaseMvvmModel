package com.ckz.library_base_compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost



/**
 *@packageName com.ckz.yyt.callnumberscreen
 *@author kzcai
 *@date 2023/11/14
 * val navController = rememberNavController()
 *  AppNav.instance.AppHost(
 *     navController = navController,
 *      startDestination = "home",
 *      ){
 *          composable("home"){
 *             Greeting("Android")
 *          }
 *          composable("second"){
 *             SecondPage()
 *          }
 *     }
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
        contentAlignment: Alignment = Alignment.Center,
        route: String? = null,
        enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
            { fadeIn(animationSpec = tween(700)) },
        exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
            { fadeOut(animationSpec = tween(700)) },
        popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
            enterTransition,
        popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
            exitTransition,
        builder: NavGraphBuilder.() -> Unit
    ) {
        _appNavController = navController
        NavHost(
            navController,
            startDestination,
            modifier,
            contentAlignment,
            route,
            enterTransition,
            exitTransition,
            popEnterTransition,
            popExitTransition,
            builder
        )
    }
}