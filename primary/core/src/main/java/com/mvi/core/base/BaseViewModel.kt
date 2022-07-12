package com.mvi.core.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T: BaseIntent> : ViewModel() {
    private var _intent: BaseIntent? = null

    protected val event: T get() = _intent as T

    abstract fun onTriggerEvent(eventType: T)
}