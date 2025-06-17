package com.ckz.library_base_compose.base

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 *@packageName com.ckz.library_base_compose.base
 *@author kzcai
 *@date 2025/6/15
 */
@Composable
fun <VM: BaseViewModel>ContentWidget(
    modifier: Modifier = Modifier,
    onRetry: OnRetry = {  },
    loading: @Composable (StateLayoutData) -> Unit = { DefaultLoadingLayout(it) },
    empty: @Composable (StateLayoutData) -> Unit = { DefaultEmptyLayout(it) },
    error: @Composable (StateLayoutData) -> Unit = { DefaultErrorLayout(it) },
    vm: VM,
    content: @Composable () -> Unit = { },

    ) {
    val showDialog = vm.isDialog
    val pageStateData = vm.pageData
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = lifecycleOwner.lifecycle

    val observer = remember {
        LifecycleEventObserver { owner, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    if (!vm.hasCreated) {
                       vm.onCreate(owner)
                        vm.hasCreated = true
//                            Log.d("HomePage", "HomePage: ${event.name}")
                    }

                }

                Lifecycle.Event.ON_START -> {
                    vm.onStart(owner)
//                        Log.d("HomePage", "HomePage: ${event.name}")
                }

                Lifecycle.Event.ON_RESUME -> {
                    vm.onResume(owner)
//                        Log.d("HomePage", "HomePage: ${event.name}")
                }

                Lifecycle.Event.ON_PAUSE -> {
                       vm.onPause(owner)
                    Log.d("HomePage", "HomePage: ${event.name}")
                }

                Lifecycle.Event.ON_STOP -> {
                    vm.onStop(owner)
//                        Log.d("HomePage", "HomePage: ${event.name}")
                }

                Lifecycle.Event.ON_DESTROY -> {
                    vm.onDestroy(owner)

//                        Log.d("HomePage", "HomePage: ${event.name}")
                    vm.hasCreated = false
                }

                Lifecycle.Event.ON_ANY -> {
                    vm.onAny(owner, event)
//                        Log.d("HomePage", "HomePage: ${event.name}")
                }
            }

        }
    }

    DisposableEffect(lifecycleOwner, observer) {
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            vm.onDestroy(lifecycleOwner)
        }
    }

    DefaultStateLayout(
        modifier = modifier,
        pageStateData = pageStateData,
        onRetry = onRetry,
        loading = loading,
        empty = empty,
        error = error,
        content = content
    )

    if (true == showDialog) {
        Dialog(
            onDismissRequest = {
                vm.isDialog = !vm.isDialog
            },
            properties = DialogProperties(
                dismissOnClickOutside = vm.canDismissByTouch,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
                dismissOnBackPress = vm.canDismissByBack//
            )
        ) {
            Column(
                modifier = Modifier

                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.White)
                Box(modifier = Modifier.height(10.dp))
                Text(text = vm.dialogText, color = Color.White)
            }

        }
    }
}