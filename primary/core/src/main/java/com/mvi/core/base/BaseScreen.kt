package com.mvi.core.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController

abstract class BaseScreen {

    protected lateinit var navController: NavHostController

    @Composable
    protected fun getContext() = LocalContext.current

    protected open fun onAnyLifecycle(lifeCycleOwner: LifecycleOwner) {}
    protected open fun onCreate(lifeCycleOwner: LifecycleOwner) {}
    protected open fun onStart(lifeCycleOwner: LifecycleOwner) {}
    protected open fun onResume(lifeCycleOwner: LifecycleOwner) {}
    protected open fun onPause(lifeCycleOwner: LifecycleOwner) {}
    protected open fun onStop(lifeCycleOwner: LifecycleOwner) {}
    protected open fun onDestroy(lifeCycleOwner: LifecycleOwner) {}

    @Composable
    open fun createScreen(navController: NavHostController) {
        this.navController = navController
        composableLifecycle(
            onEvent = { lifecycleOwner, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> onCreate(lifecycleOwner)
                    Lifecycle.Event.ON_START -> onStart(lifecycleOwner)
                    Lifecycle.Event.ON_RESUME -> onResume(lifecycleOwner)
                    Lifecycle.Event.ON_PAUSE -> onPause(lifecycleOwner)
                    Lifecycle.Event.ON_STOP -> onStop(lifecycleOwner)
                    Lifecycle.Event.ON_DESTROY -> onDestroy(lifecycleOwner)
                    Lifecycle.Event.ON_ANY -> onAnyLifecycle(lifecycleOwner)
                }
            }
        )
        onScreenCreated()
    }

    @Composable
    protected abstract fun onScreenCreated()

    protected fun getArgument(
        navController: NavHostController
    ) = navController.previousBackStackEntry?.savedStateHandle

    protected fun putArgument(
        navController: NavHostController,
        savedStateHandle: SavedStateHandle.() -> Unit
    ) {
        navController.currentBackStackEntry?.savedStateHandle?.let {
            savedStateHandle.invoke(it)
        }
    }

    @Composable
    private fun composableLifecycle(
        lifeCycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
        onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
    ) {
        DisposableEffect(lifeCycleOwner) {
            val observer = LifecycleEventObserver { source, event ->
                onEvent(source, event)
            }
            lifeCycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifeCycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}